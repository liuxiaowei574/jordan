/**
 * 
 */
package com.nuctech.ls.model.memcached;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.schooner.MemCached.ObjectTransCoder;
import com.schooner.MemCached.SockOutputStream;

/**
 * @author sunming
 *
 */
public class MemcachedTransCoder extends ObjectTransCoder {

    private Class<?> className;

    public MemcachedTransCoder(Class<?> className) {
        // TODO Auto-generated constructor stub
        this.className = className;
    }

    @Override
    public Object decode(InputStream arg0, ClassLoader arg1) throws IOException {
        // TODO Auto-generated method stub
        return super.decode(arg0, arg1);
    }

    @Override
    public Object decode(InputStream arg0) throws IOException {
        // TODO Auto-generated method stub
        if (className != null) {
            return decode(arg0, className.getClassLoader());
        } else {
            return super.decode(arg0);
        }
    }

    @Override
    public void encode(OutputStream arg0, Object arg1) throws IOException {
        // TODO Auto-generated method stub
        super.encode(arg0, arg1);
    }

    @Override
    public int encode(SockOutputStream arg0, Object arg1) throws IOException {
        // TODO Auto-generated method stub
        return super.encode(arg0, arg1);
    }

}
