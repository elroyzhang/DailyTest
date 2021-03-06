package net.thrift.simpleExample.TestProcessor;


import net.thrift.simpleExample.service.Test;
import net.thrift.simpleExample.service.TestImpl;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

public class Server {
  public void startServer() {
    try {

      TServerSocket serverTransport = new TServerSocket(1234);

      Test.Processor process = new Test.Processor(new TestImpl());

      Factory portFactory = new TBinaryProtocol.Factory(true, true);

      Args args = new Args(serverTransport);
      args.processor(process);
      args.protocolFactory(portFactory);

      TServer server = new TThreadPoolServer(args);
      server.serve();
    } catch (TTransportException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    Server server = new Server();
    server.startServer();
  }
}