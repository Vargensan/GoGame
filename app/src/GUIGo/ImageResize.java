package src.GUIGo;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

/**
 * Created by Bartłomiej Woś on 2016-12-10.
 */
public class ImageResize {

    /**
     * Method that takes image and rescale it
     * @param image takes original image to rescale
     * @param areaWidth takes preferred width of image
     * @param areaHeight take preferred height of image
     * @return rescaled image
     */

    public BufferedImage scale(BufferedImage image, int areaWidth, int areaHeight){
        double scaleX = (double) areaWidth/image.getWidth();
        double scaleY = (double) areaHeight/image.getHeight();
        double scale  = Math.min(scaleX,scaleY);
        int w = Math.round(image.getWidth() *(float) scale);
        int h = Math.round(image.getHeight() *(float) scale);
        int type = image.getTransparency() == Transparency.OPAQUE ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        boolean scaledown = scale < 1;
        if(scaledown){
            int currentW = image.getWidth();
            int currentH = image.getHeight();
            BufferedImage resized = image;
            while(currentW > w || currentH > h){
                currentW = Math.max(w,currentW / 2);
                currentH = Math.max(h, currentH / 2);

                BufferedImage temp = new BufferedImage(currentW,currentH,type);
                Graphics2D g2 = temp.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(resized,0,0,currentW,currentH,null);
                g2.dispose();
                resized = temp;
            }
            return resized;
        } else{
            Object hint = scale > 2 ? RenderingHints.VALUE_INTERPOLATION_BICUBIC : RenderingHints.VALUE_INTERPOLATION_BILINEAR;
            BufferedImage resized = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2  = resized.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,hint);
            g2.drawImage(image,0,0,w,h,null);
            g2.dispose();
            return resized;
        }
    }
}
