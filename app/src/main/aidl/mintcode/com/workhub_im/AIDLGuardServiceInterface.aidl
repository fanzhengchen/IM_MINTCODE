// AIDLGuardServiceInterface.aidl
package mintcode.com.workhub_im;

// Declare any non-default types here with import statements

interface AIDLGuardServiceInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    /**
     *  开启service
     */
    void startService();


    /**
     * 检测心跳
     */
    void keepBeet();

    /**
     *  关闭service
     */
    void stopService();
}
