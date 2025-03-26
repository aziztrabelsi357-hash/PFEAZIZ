package com.example.PfaBack.Services;

import ai.onnxruntime.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RabiesDetectionService {

    private final OrtEnvironment env;
    private final OrtSession session;
    private final List<String> classNames = List.of(
            "Incoordination",
            "barking",
            "bone in throat syndrome",
            "digging",
            "dropped jaw_ toungh",
            "hyper_salivation"
    );

    public RabiesDetectionService() throws OrtException {
        this.env = OrtEnvironment.getEnvironment();
        ClassPathResource modelResource = new ClassPathResource("models/best.onnx");
        File modelFile;
        try {
            modelFile = modelResource.getFile();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load ONNX model file", e);
        }
        this.session = env.createSession(modelFile.getAbsolutePath(), new OrtSession.SessionOptions());
    }

    public Map<String, Object> detectRabies(File imageFile) throws Exception {
        BufferedImage image = ImageIO.read(imageFile);
        BufferedImage resizedImage = resizeImage(image, 640, 640);
        float[] imageData = preprocessImage(resizedImage);

        long[] inputShape = new long[]{1, 3, 640, 640};
        OnnxTensor inputTensor = OnnxTensor.createTensor(env, FloatBuffer.wrap(imageData), inputShape);

        Map<String, OnnxTensor> inputs = new HashMap<>();
        inputs.put("images", inputTensor);

        try (OrtSession.Result results = session.run(inputs)) {
            float[][][] output = (float[][][]) results.get(0).getValue();
            return postProcessOutput(output, image.getWidth(), image.getHeight());
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        resizedImage.getGraphics().drawImage(originalImage.getScaledInstance(targetWidth, targetHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
        return resizedImage;
    }

    private float[] preprocessImage(BufferedImage image) {
        float[] result = new float[3 * 640 * 640];
        int index = 0;

        for (int c = 0; c < 3; c++) {
            for (int h = 0; h < 640; h++) {
                for (int w = 0; w < 640; w++) {
                    int rgb = image.getRGB(w, h);
                    float value = (c == 0 ? (rgb >> 16) & 0xFF : c == 1 ? (rgb >> 8) & 0xFF : rgb & 0xFF);
                    result[index++] = value / 255.0f;
                }
            }
        }
        return result;
    }

    private Map<String, Object> postProcessOutput(float[][][] output, int originalWidth, int originalHeight) {
        float maxConfidence = 0.0f;
        String detectedSymptom = "None";
        int bestClass = -1;
        float[] bestBox = new float[]{0, 0, 0, 0}; // [x, y, w, h]

        System.out.println("Output shape: [" + output.length + ", " + output[0].length + ", " + output[0][0].length + "]");

        // Process detections
        for (int i = 0; i < output[0].length; i++) {
            float confidence = output[0][i][4]; // Confidence score
            if (confidence > maxConfidence && confidence > 0.3) { // Apply a minimum confidence threshold
                maxConfidence = confidence;
                // Class scores
                float[] classScores = new float[classNames.size()];
                for (int j = 0; j < classNames.size(); j++) {
                    classScores[j] = output[0][i][5 + j];
                }
                // Find the class with the highest score
                bestClass = 0;
                for (int j = 1; j < classScores.length; j++) {
                    if (classScores[j] > classScores[bestClass]) {
                        bestClass = j;
                    }
                }
                detectedSymptom = classNames.get(bestClass);
                // Extract bounding box (x, y, w, h)
                bestBox[0] = output[0][i][0]; // x
                bestBox[1] = output[0][i][1]; // y
                bestBox[2] = output[0][i][2]; // w
                bestBox[3] = output[0][i][3]; // h
            }
        }

        // Scale bounding box coordinates back to the original image dimensions
        float scaleX = (float) originalWidth / 640.0f;
        float scaleY = (float) originalHeight / 640.0f;
        Map<String, Float> boundingBox = new HashMap<>();
        boundingBox.put("x", bestBox[0] * scaleX);
        boundingBox.put("y", bestBox[1] * scaleY);
        boundingBox.put("width", bestBox[2] * scaleX);
        boundingBox.put("height", bestBox[3] * scaleY);

        Map<String, Object> result = new HashMap<>();
        result.put("confidence", maxConfidence); // Keep confidence in [0, 1] range
        result.put("symptom", detectedSymptom);
        result.put("boundingBox", boundingBox);

        if (maxConfidence >= 0.5) {
            result.put("message", "Your dog probably has rabies. Take care and take him to the nearest clinic.");
            result.put("status", "warning");
        } else {
            result.put("message", "Your dog seems okay, but you should monitor him more closely.");
            result.put("status", "info");
        }

        return result;
    }
}