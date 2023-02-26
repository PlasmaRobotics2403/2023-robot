package frc.robot;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;

public class LEDs {
    private AddressableLED LED;
    private AddressableLEDBuffer LEDBuffer;
    // Store what the last hue of the first pixel is
    private int firstPixelHue;

    public enum LEDStripStatus {
        OFF,
        ON
    }

    public LEDStripStatus stripStatus;

    public LEDs() {
        LED = new AddressableLED(0);
        LEDBuffer = new AddressableLEDBuffer(144);
    
        LED.setLength(LEDBuffer.getLength());

        stripStatus = LEDStripStatus.ON;
    
        LED.setData(LEDBuffer);
        LED.start();
    }
    public void Rainbow() {
        for (var i = 0; i < LEDBuffer.getLength(); i++) {
            final var hue = (firstPixelHue + (i * 180 / LEDBuffer.getLength())) % 180;
            LEDBuffer.setHSV(i, hue, 255, 128);
        }
    
          firstPixelHue += 3;
          firstPixelHue %= 180;

          LED.setData(LEDBuffer);
    }
    
    public void setHSV(int i, int hue, int saturation, int value) {
        LEDBuffer.setHSV(i, hue, saturation, value);
    }

    public void setRGB(int i, int red, int green, int blue) {
        LEDBuffer.setRGB(i, red, green, blue);
    }

    public void setRGB(int red, int green, int blue) {
        for (int i = 0; i < getBufferLength(); i++) {
            LEDBuffer.setRGB(i, red, green, blue);
        }
    }

    public int getBufferLength() {
        return LEDBuffer.getLength();
    }

    public void sendData() {
        LED.setData(LEDBuffer);
    }

    public void stopLED() {
        LED.stop();
        stripStatus = LEDStripStatus.OFF;
    }

    public void startLED() {
        LED.start();
        stripStatus = LEDStripStatus.ON;
    }
}
