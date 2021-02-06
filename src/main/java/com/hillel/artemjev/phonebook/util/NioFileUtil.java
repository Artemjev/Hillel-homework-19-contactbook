package com.hillel.artemjev.phonebook.util;

import lombok.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;


@AllArgsConstructor
public class NioFileUtil {
    private final Path path;
    @Getter
    @Setter
    private int bufferCapacity;

    public NioFileUtil(Path path) {
        this(path, 10);
    }

    public void readByLine(Consumer<String> onLine) {
        try (ByteChannel channel = Files.newByteChannel(path, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(bufferCapacity);
            String stringBuffer = "";

            while (channel.read(buffer) != -1) {
                buffer.flip();
                stringBuffer += getStringFromBuffer(buffer);
                String[] parts = stringBuffer.split("\n");
                for (int i = 0; i < parts.length - 1; i++) {
                    onLine.accept(parts[i]);
                }

                if (!stringBuffer.endsWith("\n")) {
                    stringBuffer = parts[parts.length - 1];
                } else {
                    onLine.accept(parts[parts.length - 1]);
                    stringBuffer = "";
                }
                buffer.clear();
            }

            onLine.accept(stringBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeString(String str) {
        try (ByteChannel channel = Files.newByteChannel(path, StandardOpenOption.WRITE, StandardOpenOption.APPEND)) {
            ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cleanFile() {
        try {
            Files.writeString(path, "", StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile() {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createFileIfNotExist() {
        try {
            Files.writeString(path, "", StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //---------------------------------------------------------------------------
    private String getStringFromBuffer(ByteBuffer buffer) {
        return new String(
                buffer.array(),
                buffer.position(),
                buffer.limit()
        );
    }
}
