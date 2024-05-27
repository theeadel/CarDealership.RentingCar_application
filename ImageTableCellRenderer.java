import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = new JLabel();
        if (value instanceof byte[]) {
            byte[] imageBytes = (byte[]) value;
            ImageIcon imageIcon = new ImageIcon(getImageFromBytes(imageBytes));
            label.setIcon(imageIcon);
        } else {
            label.setText(value.toString());
        }

        return label;
    }

    public Image getImageFromBytes(byte[] imageData) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageData)) {
            return ImageIO.read(bis);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}