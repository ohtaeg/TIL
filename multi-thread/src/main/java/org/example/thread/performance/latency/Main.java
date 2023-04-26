package org.example.thread.performance.latency;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final String SOURCE_FILE = "./multi-thread/src/main/resources/many-flowers.jpg";
    public static final String DESTINATION_FILE = "./multi-thread/src/main/resources/many-purples-flowers.jpg";

    public static void main(String[] args) throws IOException {
        // 픽셀, 컬러스페이스, 디멘션 등 이미지 데이터 표현과 픽셀 조작이 가능한 이미지 객체
        BufferedImage original = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage output = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);

        long startTime = System.currentTimeMillis();
        // recolorSingleThreaded(original, output);
        recolorMultiThreaded(original, output, 8);
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);

        File result = new File(DESTINATION_FILE);
        ImageIO.write(output, "jpg", result);
    }

    // 싱글 스레드를 통해 왼쪽 상단부터 색칠
    public static void recolorSingleThreaded(BufferedImage originalImage, BufferedImage resultImage) {
        recolorImage(originalImage, resultImage, 0, 0, originalImage.getWidth(), originalImage.getHeight());
    }

    // 멀티 스레드를 통해 전체 높이를 스레드 수로 나눠서 이미지를 분할하여 병렬 처리
    public static void recolorMultiThreaded(BufferedImage originalImage, BufferedImage resultImage, int numberOfThreads) {
        List<Thread> threads = new ArrayList<>();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight() / numberOfThreads;

        for (int i = 0; i < numberOfThreads; i++) {
            final int threadMultiplier = i;
            threads.add(new Thread(() -> {
                int leftCorner = 0;
                int topCorner = height * threadMultiplier;
                recolorImage(originalImage, resultImage, leftCorner, topCorner, width, height);
            }));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * 전체 이미지 픽셀을 반복하여 recoloring 적용
     * @param originalImage 원본 이미지
     * @param resultImage 결과 이미지
     * @param leftCorner 좌측 값
     * @param topCorner 상단 코너 값
     * @param width 넓이
     * @param height 높이
     */
    public static void recolorImage(BufferedImage originalImage, BufferedImage resultImage, int leftCorner, int topCorner,
                                    int width, int height) {
        // 좌측 코너와 행 끝간 사이의 모든 x 값을 반복
        for (int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth(); x++) {
            // x 값에 해당하는 상단 - 하단 열 값을 반복
            for (int y = topCorner; y < topCorner + height && y < originalImage.getHeight(); y++) {
                recolorPixel(originalImage, resultImage, x, y);
            }
        }
    }

    /**
     * 원본 이미지, 결과 이미지를 받고 원하는 픽셀의 x, y 좌표에 픽셀 색상을 다시 칠하는 메서드
     */
    public static void recolorPixel(BufferedImage original, BufferedImage output, int x, int y) {
        int rgb = original.getRGB(x, y);
        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        int newRed = red;
        int newGreen = green;
        int newBlue = blue;

        // 원본 이미지의 특정 좌표의 픽셀이 회색 계열이라면 보라색으로, (red + 10, green - 80, blue - 20)
        if (isShadeOfGray(red, green, blue)) {
            newRed = Math.min(255, red + 10);
            newGreen = Math.max(0, green - 80);
            newBlue = Math.max(0, blue - 20);
        }

        int newRGB = createRGBFromColors(newRed, newGreen, newBlue);
        setRGB(output, x, y, newRGB);
    }

    /**
     * 픽셀의 특정 색상 값을 얻어와 픽셀에 넣을 회색을 결정
     */
    public static boolean isShadeOfGray(int red, int green, int blue) {
        final int approximation = 30;
        // 모든 컴포넌트가 같은 색상 강도를 갖는지 근사치를 통해 확인, == 균등 == 회색
        return Math.abs(red - green) < approximation
                & Math.abs(red - blue) < approximation
                & Math.abs(green - blue) < approximation;
    }

    /**
     * RGB 개별 컴포넌트 값을 합쳐 픽셀에 넣는다.
     */
    public static int createRGBFromColors(int red, int green, int blue) {
        int rgb = 0;
        // blue가 ARGB에서 젤 오른쪽에 있으니 RGB 값과 blue 값 사이에 or 연산자를 통해 추가
        rgb |= blue;

        // green 값은 비트 시프트 왼쪽으로 이동
        rgb |= green << 8;

        // red 값은 왼쪽으로 16비트 만큼 이동
        rgb |= red << 16;

        // 픽셀을 불투명하게, 불투명하게 하기 위해서는 Alpha 값을 최대로 설정
        rgb |= 0xFF000000; // 255

        return rgb;
    }

    /**
     * 새로 생성한 RGB 값을 BufferedImage 객체에 할당
     */
    public static void setRGB(BufferedImage image, int x, int y, int rgb) {
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

    /**
     * 왼쪽으로 16비트만큼 이동하여 픽셀에서 빨강만 추출
     */
    public static int getRed(int rgb) {
        return (rgb & 0x00FF0000) >> 16;
    }

    /**
     * 픽셀에서 그린만 추출한다.
     * 픽셀에 Alpha, Red, Blue에다 마스킹하여 컴포넌트를 다 0으로 설정하고 0x0000FF00를 적용
     */
    public static int getGreen(int rgb) {
        return (rgb & 0x0000FF00) >> 8;
    }

    /**
     * 픽셀에서 파랑만 추출한다.
     * 픽셀에다 비트마스크를 적용하여 컴포넌트를 다 0으로 설정하고 0x000000FF를 적용
     */
    public static int getBlue(int rgb) {
        return rgb & 0x000000FF;
    }

}
