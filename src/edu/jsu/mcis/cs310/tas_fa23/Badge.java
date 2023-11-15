package edu.jsu.mcis.cs310.tas_fa23;

import java.util.zip.CRC32;

public class Badge {

    private final String id, description;

    // Existing constructor
    public Badge(String id, String description) {
        this.id = id;
        this.description = description;
    }

    // New constructor for creating Badge from description and generating ID
    public Badge(String description) {
        this.id = generateBadgeID(description);
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    // Helper method to generate a badge ID using CRC-32 checksum
    private String generateBadgeID(String description) {
        CRC32 crc32 = new CRC32();
        crc32.update(description.getBytes());
        long checksum = crc32.getValue();
        return String.format("#%08X", checksum);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append('#').append(id).append(' ');
        s.append('(').append(description).append(')');
        return s.toString();
    }
}

