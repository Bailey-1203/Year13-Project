package utility;

import org.joml.Vector2f;

public class Cal {

    public static void rotate(Vector2f vec, float angleDeg, Vector2f origin) {
        float x = vec.x - origin.x;
        float y = vec.y - origin.y;

        float cos = (float) Math.cos(Math.toRadians(angleDeg));
        float sin = (float) Math.sin(Math.toRadians(angleDeg));

        float xPrime = (x * cos) - (y * sin);
        float yPrime = (x * sin) + (y * cos);

        xPrime += origin.x;
        yPrime += origin.y;

        vec.x = xPrime;
        vec.y = yPrime;
    }

    public static boolean compare(float x, float y, float epsilon) {
        return Math.abs(x - y) <= epsilon * Math.max(1.0f, Math.max(Math.abs(x), Math.abs(y)));
    }

    public static boolean compare(Vector2f vec1, Vector2f vec2, float epsilon) {
        return compare(vec1.x, vec2.x, epsilon) && compare(vec1.y, vec2.y, epsilon);
    }

    public static boolean compare(float x, float y) {
        return Math.abs(x - y) <= Float.MIN_VALUE * Math.max(1.0f, Math.max(Math.abs(x), Math.abs(y)));
    }

    public static boolean compare(Vector2f vec1, Vector2f vec2) {
        return compare(vec1.x, vec2.x) && compare(vec1.y, vec2.y);
    }

    public static float DotProduct(Vector2f a, Vector2f b) {
        final float number = a.x * b.x + a.y * b.y;
        return number;
    }

    public static float AbsoluteValue(Vector2f a, Vector2f b) {
        final float number = (float) (Math.sqrt(Math.pow(a.x, 2) + Math.pow(a.y, 2)) * Math.sqrt(Math.pow(b.x, 2) + Math.pow(b.y, 2)));
        return number;
    }

    public static float AngleBetweenVectors(Vector2f a, Vector2f b) {
        float x = DotProduct(a, b);
        float y = AbsoluteValue(a, b);
        float number = (float) Math.acos(x / y);
        return number;
    }
}
