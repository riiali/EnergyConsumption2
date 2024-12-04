package org.EnergyConsArticle;

import org.EnergyConsArticle.VisitorPattern.*;
import org.EnergyConsArticle.Unpatterned.*;

import java.io.FileWriter;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class Main {
    private static final String SHELLY_IP = "192.168.1.183"; // Shelly Plug IP address
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    public static void main(String[] args) throws IOException, InterruptedException {
        int iterations = 1000; // Number of iterations

        try (FileWriter validatorWriter = new FileWriter("validator_consumption.csv");
             FileWriter nonValidatorWriter = new FileWriter("non_validator_consumption.csv");
             FileWriter totalWriter = new FileWriter("total_consumption.csv")) {

            // Write CSV headers
            writeHeaders(validatorWriter);
            writeHeaders(nonValidatorWriter);
            writeTotalHeaders(totalWriter);

            System.out.println("Running Validator...");
            double totalValidatorConsumption = runPattern(
                    "Validator",
                    Main::runValidatorPatternTest,
                    validatorWriter,
                    iterations
            );

            System.out.println("Running NonValidator...");
            double totalNonValidatorConsumption = runPattern(
                    "NonValidator",
                    Main::runUnpatternedTest,
                    nonValidatorWriter,
                    iterations
            );

            // Write total consumption
            totalWriter.append("Pattern;Total Consumption (kWh)\n");
            totalWriter.append("Validator;" + totalValidatorConsumption + "\n");
            totalWriter.append("NonValidator;" + totalNonValidatorConsumption + "\n");

            System.out.println("Total Validator Consumption: " + totalValidatorConsumption + " kWh");
            System.out.println("Total NonValidator Consumption: " + totalNonValidatorConsumption + " kWh");
        }
    }

    private static void runValidatorPatternTest() {
        Dot dot = new Dot(1, 10, 55);
        Circle circle = new Circle(2, 23, 15, 10);
        Rectangle rectangle = new Rectangle(3, 10, 17, 20, 30);

        CompoundShape compoundShape = new CompoundShape(4);
        compoundShape.add(dot);
        compoundShape.add(circle);
        compoundShape.add(rectangle);

        CompoundShape c = new CompoundShape(5);
        c.add(dot);
        compoundShape.add(c);

        exportValidator(circle, compoundShape);
    }

    private static void exportValidator(Shape... shapes) {
        XMLExportVisitor exportVisitor = new XMLExportVisitor();
        exportVisitor.export(shapes); // Simulate the export (output suppressed for performance)
    }

    private static void runUnpatternedTest() {
        UnpatternedDot dot = new UnpatternedDot(1, 10, 55);
        UnpatternedCircle circle = new UnpatternedCircle(2, 23, 15, 10);
        UnpatternedRectangle rectangle = new UnpatternedRectangle(3, 10, 17, 20, 30);

        UnpatternedCompoundShape compoundShape = new UnpatternedCompoundShape(4);
        compoundShape.add(dot);
        compoundShape.add(circle);
        compoundShape.add(rectangle);

        UnpatternedCompoundShape c = new UnpatternedCompoundShape(5);
        c.add(dot);
        compoundShape.add(c);

        exportUnpatterned(circle, compoundShape);
    }

    private static void exportUnpatterned(UnpatternedShape... shapes) {
        for (UnpatternedShape shape : shapes) {
            shape.exportXML(); // Simulate the export (output suppressed for performance)
        }
    }

    private static double runPattern(String patternName, Runnable task, FileWriter writer, int iterations) throws IOException, InterruptedException {
        double totalConsumption = 0.0;

        for (int i = 1; i <= iterations; i++) {
            System.out.println("Iteration: " + i);
            long startTime = System.nanoTime(); // Start time in nanoseconds

            String startData = getShellyData();
            task.run(); // Execute the test task
            Thread.sleep(1000); // Delay to ensure energy data is updated
            String endData = getShellyData();

            long endTime = System.nanoTime(); // End time in nanoseconds
            double elapsedTimeInSeconds = (endTime - startTime) / 1_000_000_000.0; // Convert nanoseconds to seconds

            // Parse data manually
            double apower = parseDoubleFromJson(startData, "\"apower\":");
            double voltage = parseDoubleFromJson(startData, "\"voltage\":");
            double current = parseDoubleFromJson(startData, "\"current\":");

            double startTotalEnergy = parseDoubleFromJson(startData, "\"total\":", "\"aenergy\":") / 1000.0; // Convert Wh to kWh
            double endTotalEnergy = parseDoubleFromJson(endData, "\"total\":", "\"aenergy\":") / 1000.0; // Convert Wh to kWh
            double iterationConsumption = endTotalEnergy - startTotalEnergy;

            totalConsumption += iterationConsumption;

            writer.append(patternName).append(";")
                    .append(String.valueOf(i)).append(";")
                    .append(String.valueOf(apower)).append(";")
                    .append(String.valueOf(voltage)).append(";")
                    .append(String.valueOf(current)).append(";")
                    .append(String.valueOf(endTotalEnergy)).append(";")
                    .append(String.valueOf(iterationConsumption)).append(";")
                    .append(String.valueOf(elapsedTimeInSeconds)).append("\n");

            System.out.println("Iteration Consumption: " + iterationConsumption + " kWh");
            System.out.println("Run Time: " + elapsedTimeInSeconds + " seconds");
        }

        return totalConsumption;
    }

    private static String getShellyData() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + SHELLY_IP + "/rpc/Shelly.GetStatus"))
                .GET()
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private static double parseDoubleFromJson(String json, String key) {
        return parseDoubleFromJson(json, key, null);
    }

    private static double parseDoubleFromJson(String json, String key, String contextKey) {
        int startIndex = json.indexOf(contextKey != null ? contextKey : key);
        if (contextKey != null) {
            startIndex = json.indexOf(key, startIndex);
        }
        if (startIndex == -1) return 0.0;

        startIndex += key.length();
        int endIndex = json.indexOf(",", startIndex);
        if (endIndex == -1) {
            endIndex = json.indexOf("}", startIndex);
        }

        try {
            return Double.parseDouble(json.substring(startIndex, endIndex).trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private static void writeHeaders(FileWriter writer) throws IOException {
        writer.append("Pattern;Iteration;Apower (W);Voltage (V);Current (A);Total Energy (kWh);Iteration Consumption (kWh);Run Time (s)\n");
    }

    private static void writeTotalHeaders(FileWriter writer) throws IOException {
        writer.append("Pattern;Total Consumption (kWh)\n");
    }
}
