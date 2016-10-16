package designmode.oberver;

/**
 * Created by elroy on 16-10-16.
 */
public interface Observe {
   public void add(Listener listen);
   public void delete(Listener listen);
   public void notifyListener(Listener listen);
}
