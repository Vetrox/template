import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class FloatTest {

    private static class FloatHolder {
        public FloatHolder() {}

        public FloatHolder(float f) {value = f;}

        public float value = 0;
    }

    public static class RawFloat {
        public boolean is_negative = false;
        public int exponentRaw = 0;
        public int mantissa = 0;

        @Override
        public String toString() {
            return "RawFloat{" + "is_negative=" + is_negative + ", exponentRaw (0 to 255)=" + exponentRaw + ", " +
                    "exponent (-126 to 128)=" + (exponentRaw - 127) + ", mantissa=" + mantissa + '}';
        }
    }

    public static float setFloat(RawFloat rawFloat) throws NoSuchFieldException, IllegalAccessException {
        int toFloat = 0;
        toFloat |= rawFloat.is_negative ? (1 << 31) : 0;
        toFloat |= rawFloat.exponentRaw << 23;
        toFloat |= rawFloat.mantissa;
        FloatHolder var = new FloatHolder();
        long fieldOffset = getUnsafe().objectFieldOffset(FloatHolder.class.getDeclaredField("value"));
        getUnsafe().getAndSetInt(var, fieldOffset, toFloat);
        return var.value;
    }

    public static RawFloat getFloat(float f) throws NoSuchFieldException, IllegalAccessException {
        FloatHolder var = new FloatHolder(f);
        long fieldOffset = getUnsafe().objectFieldOffset(FloatHolder.class.getDeclaredField("value"));
        int rawFloat = getUnsafe().getInt(var, fieldOffset);
        RawFloat rf = new RawFloat();
        rf.is_negative = (rawFloat & (1 << 31)) != 0;
        rf.exponentRaw = (rawFloat >>> 23) & 0xff;
        rf.mantissa = rawFloat & 0x7fffff;
        return rf;
    }

    public static Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        return (Unsafe) unsafeField.get(null);
    }

    @Test
    public void test() throws NoSuchFieldException, IllegalAccessException {
        RawFloat rf = new RawFloat();
        for (int i = 0; i < 10; i++) {
            System.out.println("setFloat(rf) = " + setFloat(rf));
            rf.mantissa++;
        }
    }

}
