package testlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by elroy on 16-8-23.
 */
public class TestLog {

    static Logger LOG = LoggerFactory.getLogger(TestLog.class);

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++)
            if (i % 2 == 0)
                LOG.info("Hello {}", i);
            else
                LOG.debug("I am on index {}", i);
    }
}
