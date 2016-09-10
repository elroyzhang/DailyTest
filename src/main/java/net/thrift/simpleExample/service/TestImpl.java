package net.thrift.simpleExample.service;

import org.apache.thrift.TException;

/**
 * Created by elroy on 16-9-9.
 */
public class TestImpl implements Test.Iface{

  @Override public void ping(int length) throws TException {
    System.out.println("calling ping , length=" + length);
  }
}
