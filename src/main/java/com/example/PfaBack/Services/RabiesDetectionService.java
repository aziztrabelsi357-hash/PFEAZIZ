package com.example.PfaBack.Services;

import ai.onnxruntime.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.FloatBuffer;
import java.util.Arrays;
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

    // this is to load the model
    public RabiesDetectionService() throws OrtException {
        try {
            this.env = OrtEnvironment.getEnvironment();
            ClassPathResource modelResource = new ClassPathResource("models/best.onnx");
            File modelFile = modelResource.getFile();
            System.out.println("Loading ONNX model from: " + modelFile.getAbsolutePath());
            if (!modelFile.exists()) {
                throw new RuntimeException("ONNX model file not found at: " + modelFile.getAbsolutePath());
            }
            this.session = env.createSession(modelFile.getAbsolutePath(), new OrtSession.SessionOptions());
            System.out.println("ONNX model loaded successfully");
        } catch (Exception e) {
            System.err.println("Failed to initialize RabiesDetectionService: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize RabiesDetectionService", e);
        }
    }
     // to analyze the image
    public Map<String, Object> detectRabies(File imageFile) throws Exception {
        try {
            System.out.println("Processing image: " + imageFile.getAbsolutePath());
            BufferedImage image = ImageIO.read(imageFile);
            if (image == null) {
                throw new RuntimeException("Failed to read image file");
            }
            BufferedImage resizedImage = resizeImage(image, 640, 640);
            float[] imageData = preprocessImage(resizedImage);

            long[] inputShape = new long[]{1, 3, 640, 640};
            OnnxTensor inputTensor = OnnxTensor.createTensor(env, FloatBuffer.wrap(imageData), inputShape);

            Map<String, OnnxTensor> inputs = new HashMap<>();
            inputs.put("images", inputTensor);

            try (OrtSession.Result results = session.run(inputs)) {
                float[][][] output = (float[][][]) results.get(0).getValue();
                return postProcessOutput(output);
            }
        } catch (Exception e) {
            System.err.println("Error during rabies detection: " + e.getMessage());
            e.printStackTrace();
            throw e;
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
     //Processing the Model’s Output
    private Map<String, Object> postProcessOutput(float[][][] output) {
        float maxConfidence = 0.0f;
        String detectedSymptom = "None";
        int bestClass = -1;

        System.out.println("Output shape: [" + output.length + ", " + output[0].length + ", " + output[0][0].length + "]");

        int numBoxes = output[0][0].length;
        int numAttributes = output[0].length;
        float[][][] transposedOutput = new float[1][numBoxes][numAttributes];

        for (int i = 0; i < numBoxes; i++) {
            for (int j = 0; j < numAttributes; j++) {
                transposedOutput[0][i][j] = output[0][j][i];
            }
        }

        System.out.println("Transposed output shape: [" + transposedOutput.length + ", " + transposedOutput[0].length + ", " + transposedOutput[0][0].length + "]");

        int expectedAttributes = classNames.size() + 5;
        if (numAttributes != expectedAttributes) {
            System.out.println("Warning: Expected " + expectedAttributes + " attributes (4 for bbox, 1 for confidence, " + classNames.size() + " for classes), but got " + numAttributes);
        }

        float[][] detections = transposedOutput[0];

        if (detections.length > 0) {
            System.out.println("Sample detection: " + Arrays.toString(detections[0]));
        }

        for (int i = 0; i < detections.length; i++) {
            int confidenceIndex = numAttributes == expectedAttributes ? 4 : 5;
            float confidence = confidenceIndex < numAttributes ? detections[i][confidenceIndex] : 0.0f;

            if (confidence < 0 || confidence > 1) {
                System.out.println("Invalid confidence value: " + confidence + " at index " + i);
                continue;
            }
            if (confidence > maxConfidence && confidence > 0.3) {
                maxConfidence = confidence;
                float[] classScores = new float[classNames.size()];
                int classStartIndex = numAttributes == expectedAttributes ? 5 : 6;
                for (int j = 0; j < classNames.size(); j++) {
                    int classIndex = classStartIndex + j;
                    if (classIndex < numAttributes) {
                        classScores[j] = detections[i][classIndex];
                    } else {
                        classScores[j] = 0.0f;
                        System.out.println("Warning: Missing class score for index " + j + " at detection " + i);
                    }
                }
                System.out.println("Class scores for detection " + i + ": " + Arrays.toString(classScores));
                bestClass = 0;
                for (int j = 1; j < classScores.length; j++) {
                    if (classScores[j] > classScores[bestClass]) {
                        bestClass = j;
                    }
                }
                detectedSymptom = classNames.get(bestClass);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("confidence", maxConfidence);
        result.put("symptom", detectedSymptom);

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