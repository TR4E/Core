package me.trae.core.database;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public final class Config {

    private final File folder;
    private final File file;
    private final YamlConfiguration config;

    public Config(final File folder, final String fileName) {
        this.folder = folder;
        this.file = new File(folder, fileName + ".yml");
        this.config = new YamlConfiguration();
    }

    public Config(final File folder, final File file) {
        this.folder = folder;
        this.file = file;
        this.config = new YamlConfiguration();
    }

    public final boolean createFile() {
        if (!folder.exists()) {
            folder.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
                return true;
            } catch (final IOException e) {
                System.out.println("Config File Error!");
                return false;
            }
        }
        return false;
    }

    public final boolean fileExists() {
        return file.exists();
    }

    public final boolean loadFile() {
        try {
            config.load(file);
            return true;
        } catch (final IOException | InvalidConfigurationException e) {
            createFile();
            e.printStackTrace();
            return false;
        }
    }

    public final boolean saveFile() {
        try {
            config.save(file);
            return true;
        } catch (final IOException e) {
            System.out.println("Failed to save File " + file.getName() + "!");
            return false;
        }
    }

    public final File getFolder() {
        return folder;
    }

    public final File getFile() {
        return file;
    }

    public final YamlConfiguration getConfig() {
        return config;
    }
}