package top.fksoft.player.android.io;

public class BeanIO {
    private volatile static BeanIO beanIO = null;

    private BeanIO() {
    }
    public static BeanIO newInstance() {
        if (beanIO == null) {
            synchronized (BeanIO.class){
                if (beanIO == null) {
                    beanIO = new BeanIO();
                }
            }
        }
        return beanIO;
    }

    

}
