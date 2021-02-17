package graphics;

import java.util.HashMap;
import java.util.Map;

import static graphics.Text.createTextTexture;
import static runner.Driver.BLACK;

public class NumberTextureGetter {
    private final static Map<Color, Map<Font, Map<Integer, Texture>>> colorMap = new HashMap();

    public static Texture getNumberTexture(int num, Font font){
        Map<Font, Map<Integer, Texture>> fontMap = colorMap.computeIfAbsent(BLACK, k -> new HashMap<>());
        Map<Integer, Texture> numTexMap = fontMap.computeIfAbsent(font, k -> new HashMap<>());

        Texture texture = numTexMap.get(num);
        if (texture == null){
            texture = createTextTexture("" + num, font);
            numTexMap.put(num, texture);
        }

        return texture;
    }

    public static Texture getNumberTexture(int num, Font font, Color color){
        Map<Font, Map<Integer, Texture>> fontMap = colorMap.computeIfAbsent(color, k -> new HashMap<>());
        Map<Integer, Texture> numTexMap = fontMap.computeIfAbsent(font, k -> new HashMap<>());

        Texture texture = numTexMap.get(num);
        if (texture == null){
            texture = createTextTexture("" + num, font);
            numTexMap.put(num, texture);
        }

        return texture;
    }
}
