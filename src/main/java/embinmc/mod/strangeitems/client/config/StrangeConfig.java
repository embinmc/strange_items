package embinmc.mod.strangeitems.client.config;

import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import embinmc.mod.strangeitems.client.StrangeItemsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class StrangeConfig {
    public static final Logger LOGGER = LoggerFactory.getLogger("strangeitems/config");
    private static final List<String> LEGACY_CONFIG = List.of("key.show_blocks_mined", "key.show_times_dropped", "key.show_mobs_killed", "key.show_tracker_ids", "key.show_time_in_dimensions");
    private StrangeConfig() {}

    public static boolean in_depth_tracking = true;
    public static boolean check_for_tooltipscroll = true;
    public static boolean invert_tooltipscroll_check_value = false;

    public static HiddenTrackers HIDDEN_TRACKERS = new HiddenTrackers(List.of());

    public static void saveConfig() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("in_depth_tracking", in_depth_tracking);
        jsonObject.addProperty("check_for_tooltipscroll_mod", check_for_tooltipscroll);
        jsonObject.addProperty("invert_tooltipscroll_check_value", invert_tooltipscroll_check_value);
        try {
            createAndWriteFile("config/strange_items.json", toFancyJsonString(jsonObject));
            createAndWriteFile("config/strange_items_hidden.json", toFancyJsonString(HiddenTrackers.CODEC.encodeStart(JsonOps.INSTANCE, HIDDEN_TRACKERS).getOrThrow()));
        } catch (Exception e) {
            LOGGER.error("Encountered an error whilst trying to save config JSON.", e);
        }
    }

    public static void readConfig() {
        try {
            FileReader reader = new FileReader("config/strange_items.json");
            JsonReader parser = new JsonReader(reader);
            parser.beginObject();
            while (parser.hasNext()) {
                final String key = parser.nextName();
                if (key.equals("in_depth_tracking")) {
                    in_depth_tracking = parser.nextBoolean();
                }
                if (key.equals("check_for_tooltipscroll_mod")) {
                    check_for_tooltipscroll = parser.nextBoolean();
                }
                if (key.equals("invert_tooltipscroll_check_value")) {
                    invert_tooltipscroll_check_value = parser.nextBoolean();
                }
                if (StrangeConfig.LEGACY_CONFIG.contains(key)) {
                    parser.nextString();
                    StrangeConfig.LOGGER.warn("Skipping legacy setting {} in config", key);
                }
            }
            parser.close();
            File hidden = new File("config/strange_items_hidden.json");
            try {
                JsonObject hiddenJson = fromInputStream(hidden.toURL().openStream());
                DataResult<HiddenTrackers> result = HiddenTrackers.CODEC.parse(JsonOps.INSTANCE, hiddenJson);
                if (result.isSuccess()) {
                    StrangeConfig.HIDDEN_TRACKERS = result.getOrThrow();
                    StrangeItemsClient.LOGGER.info(StrangeConfig.HIDDEN_TRACKERS.toString());
                    return;
                }
                if (result.error().isPresent()) {
                    StrangeConfig.LOGGER.error(String.valueOf(result.error().get()));
                }
                throw new JsonParseException("strange_items_hidden.json is not valid");
            } catch (Exception e) {
                StrangeConfig.LOGGER.error("Failed to get config for hidden trackers!", e);
                createAndWriteFile("config/strange_items_hidden.json", toFancyJsonString(HiddenTrackers.CODEC.encodeStart(JsonOps.INSTANCE, HIDDEN_TRACKERS).getOrThrow()));
            }
        } catch (FileNotFoundException e) {
            saveConfig();
        } catch (Exception e) {
            LOGGER.error("Unknown exception when trying to read config file.", e);
        }
    }

    private static String toFancyJsonString(JsonElement json) {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.setFormattingStyle(FormattingStyle.PRETTY);
        jsonWriter.setStrictness(Strictness.LENIENT);
        try {
            Streams.write(json, jsonWriter);
        } catch (IOException e) {
            StrangeConfig.LOGGER.error("Failed to format config json!", e);
            return json.toString();
        }
        return stringWriter.toString();
    }

    private static void createAndWriteFile(String path, String content) {
        try {
            Files.createDirectories(Paths.get("config"));
        } catch (IOException e) {
            StrangeConfig.LOGGER.error("Couldn't create config directory!", e);
            return;
        }
        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write(content);
            fileWriter.close();
            StrangeConfig.LOGGER.info("Wrote to file \"{}\"", path);
        } catch (IOException e) {
            StrangeConfig.LOGGER.error("Failed to create config file!", e);
        }
    }

    public static JsonObject fromInputStream(InputStream inputStream) {
        return new GsonBuilder().create().fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), JsonObject.class);
    }
}
