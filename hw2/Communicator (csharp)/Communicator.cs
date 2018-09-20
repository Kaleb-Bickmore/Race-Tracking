using System;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using log4net;

namespace Base
{
    public class Communicator
    {
        public delegate void MessageHandler(string message, IPEndPoint senderEndPoint);

        public event MessageHandler IncomingMessage;

        private IPEndPoint _localEndPoint;
        private UdpClient _udpClient;
        private Thread _myThread;
        private bool _keepGoing;

        /// <summary>
        /// Default Constructor, which opens a UDP socket on any available port
        /// </summary>
        public Communicator()
        {
            Initialize();
        }

        /// <summary>
        /// Port-specific Constructor, which opens an UDP socket on a given port.  If that port is being used
        /// by another process on the computer, this constructor will throw an exception.
        /// </summary>
        /// <param name="localPort">If non-zero, the communicator will attempt to use this port</param>
        public Communicator(int localPort)
        {
            LocalPort = localPort;
            Initialize();
        }

        /// <summary>
        /// Get the local port of the UDP socket.
        /// </summary>
        public int LocalPort { get; set; }

        /// <summary>
        /// Send a message to a target process
        /// </summary>
        /// <param name="message">Message to send</param>
        /// <param name="targetEndPoint">End point where the messasge needs to be sent</param>
        public void Send(string message, IPEndPoint targetEndPoint)
        {
            if (string.IsNullOrEmpty(message))
                throw new ApplicationException("Cannot send an empty messasge");

            if (targetEndPoint ==null || targetEndPoint.Address.ToString()=="0.0.0.0")
                throw new ApplicationException("Invalid target end point");

            var data = Encoding.BigEndianUnicode.GetBytes(message);
            _udpClient.Send(data, data.Length, targetEndPoint);
        }

        /// <summary>
        /// Check to see if messages are available
        /// </summary>
        /// <returns></returns>
        public bool IsMessageAvailable()
        {
            return _udpClient.Available>0;
        }

        /// <summary>
        /// Get a message (within the specified timeout)
        /// </summary>
        /// <param name="senderEndPoint">sender's end point (output)</param>
        /// <param name="timeout">Maximum number of milliseconds to wait for a timeout</param>
        /// <returns></returns>
        public string GetMessage(out IPEndPoint senderEndPoint, int timeout = 100)
        {
            _udpClient.Client.ReceiveTimeout = timeout;
            senderEndPoint = new IPEndPoint(IPAddress.Any, 0);
            byte[] data = null;
            try
            {
                data = _udpClient.Receive(ref senderEndPoint);
            }
            catch (SocketException err)
            {
                if (err.SocketErrorCode != SocketError.TimedOut) throw;
            }

            return (data==null || data.Length == 0) ? null :
                            Encoding.BigEndianUnicode.GetString(data, 0, data.Length);
        }

        /// <summary>
        /// Start this Communicator as an active object.  This create a thread on which the Run methods is
        /// executed
        /// </summary>
        public void Start()
        {
            if (_keepGoing) return;

            _keepGoing = true;
            _myThread = new Thread(Run);
            _myThread.Start();
        }

        /// <summary>
        /// Stop this object, if it is being used as a active object.  This causes the Run method finish up and
        /// the thread to terminate.
        /// </summary>
        public void Stop()
        {
            _keepGoing = false;
        }

        /// <summary>
        /// Close any resources used by this communicator, namely the UDP socket
        /// </summary>
        public void Close()
        {
            _udpClient?.Close();
        }

        private void Initialize()
        {
            _localEndPoint = new IPEndPoint(IPAddress.Any, LocalPort);
            _udpClient = new UdpClient(_localEndPoint);
            _localEndPoint = _udpClient.Client.LocalEndPoint as IPEndPoint;
            LocalPort = _localEndPoint?.Port ?? 0;
        }

        private void Run()
        {
            while (_keepGoing)
            {
                IPEndPoint senderEndPoint;
                var message = GetMessage(out senderEndPoint);
                if (message == null) continue;

                IncomingMessage?.Invoke(message, senderEndPoint);
            }
        }
    }
}
