
package fr.ul.acl.model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VisualEffect {

    private final List<BufferedImage> frames = new ArrayList<>();
    private final List<Integer> delays = new ArrayList<>();
    private int currentFrame = 0;

    public VisualEffect(String path) {
        ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();

        try (InputStream stream =
                getClass().getClassLoader().getResourceAsStream(path)) {

            if (stream == null)
                throw new IOException("Effect not found: " + path);

            try (ImageInputStream imageStream =
                    ImageIO.createImageInputStream(stream)) {

                reader.setInput(imageStream);

                int count = reader.getNumImages(true);

                for (int i = 0; i < count; i++) {
                    frames.add(reader.read(i));

                    IIOMetadata metadata = reader.getImageMetadata(i);
                    Node root = metadata.getAsTree("javax_imageio_gif_image_1.0");

                    NodeList nodes = ((IIOMetadataNode) root)
                            .getElementsByTagName("GraphicControlExtension");

                    IIOMetadataNode control = (IIOMetadataNode) nodes.item(0);

                    int delay = Integer.parseInt(
                            control.getAttribute("delayTime")
                    );

                    delays.add(delay * 10);
                }
            }

        } catch (IOException e) {
            System.err.println("Failed to load visual effect: " + path);
            e.printStackTrace();

        } finally {
            reader.dispose();
        }
    }

    public BufferedImage getCurrentFrame() {
        return frames.get(currentFrame);
    }

    public int getCurrentDelay() {
        return delays.get(currentFrame);
    }

    public boolean isFinished() {
        return currentFrame == frames.size() - 1;
    }

    public void nextFrame() {
        if (!isFinished())
            currentFrame++;
    }

    public void reset() {
        currentFrame = 0;
    }
}
