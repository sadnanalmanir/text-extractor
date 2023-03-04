package uk.ac.rothamsted.ide.text.extractor;

import com.martiansoftware.jsap.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    static Set<String> processOnlyDocuments = new HashSet<>(Collections.singletonList(
            "x"
    ));

    public static void main(String[] args) throws IOException {

        boolean hasArguments = false;

        if (args.length != 0) {
            hasArguments = args.length != 1 || !args[0].equals("${args}");
        }

        if (hasArguments) {
            String arguments = Arrays.toString(args).replace(", ", " ");
            arguments = arguments.substring(1, arguments.length() - 1);
            logger.info("ARGUMENTS: " + arguments + "\n");
            runExtractor(arguments);
        } else {
            System.out.println("No arguments. See README");
        }
        System.out.println("\nAll done.");
    }

    private static void runExtractor(String args) throws IOException {

        try {
            long time = System.currentTimeMillis();

            boolean runExtract;
            String inputFileOrDirName;
            String outputDirName;


            {
                JSAP jsap = new JSAP();
                // set command options
                {
                    Switch s = new Switch("runExtract").setLongFlag("extract");
                    s.setHelp("runExtract");
                    jsap.registerParameter(s);
                }
                {
                    FlaggedOption s = new FlaggedOption("input").setStringParser(JSAP.STRING_PARSER).setLongFlag("input").setShortFlag('i');
                    s.setHelp("Input file or directory");
                    jsap.registerParameter(s);
                }
                {
                    FlaggedOption s = new FlaggedOption("output").setStringParser(JSAP.STRING_PARSER).setLongFlag("output").setShortFlag('o');
                    s.setHelp("Output file or directory");
                    jsap.registerParameter(s);
                }
                // parse command
                JSAPResult config = jsap.parse(args);
                // Display helper messages
                if (!config.success()) {
                    System.err.println();
                    System.err.println(" " + jsap.getUsage());
                    System.err.println();
                    System.err.println(jsap.getHelp());
                    System.exit(1);
                }
                // assign options and switch
                inputFileOrDirName = config.getString("input");
                if (inputFileOrDirName != null) {
                    logger.info("Input corpus path: " + inputFileOrDirName);
                }
                outputDirName = config.getString("output");
                if (outputDirName != null) {
                    logger.info("Output directory path: " + outputDirName);
                }
                runExtract = config.getBoolean("runExtract");
                if (runExtract) {
                    logger.info("runExtract: " + true + "\n");
                }

                // Create output directory if it does not exist
                File outputDir = null;
                if (outputDirName != null) {
                    outputDir = Utils.createFileOrDirIfNotExist(outputDirName);
                }

                if (runExtract) {

                    Map<String, String> fileIndex = Utils.initFileIndex(inputFileOrDirName);
                    int numOfFilesToProcess = fileIndex.size();

                    if (processOnlyDocuments.size() > 1) {
                        numOfFilesToProcess = processOnlyDocuments.size() - 1;
                    }
                    int numOfFilesProcessed = 0;

                    TextExtractor textExtractor = new TextExtractor();
                    EncodingCleaner encodingCleaner = new EncodingCleaner();

                    for (String fileName : fileIndex.keySet()) {
                        if (processOnlyDocuments.size() > 1 && !processOnlyDocuments.contains(fileName)) {
                            continue;
                        }
                        numOfFilesProcessed++;

                        logger.info("Extracting text from\n===============================================================\n"
                                + "Document (" + numOfFilesProcessed + " of " + numOfFilesToProcess + "): " + fileName + " AT " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())

                                + "\n===============================================================\n");

                        String extractedText = textExtractor.extractText(fileIndex.get(fileName));
                        // Clean encoding (bad and non-ASCII characters).
                        String cleanText = EncodingCleaner.cleanEncoding(extractedText);


                        cleanText = cleanText.replaceAll("\\\\", " ");

                        logger.info("Text extraction time for document took " + (System.currentTimeMillis() - time) / 1000 + " seconds = " + (System.currentTimeMillis() - time) / 1000 / 60 + " minutes\n");

                        if (outputDir != null) {
                            Utils.saveToFile(cleanText, outputDir + "/" + fileName + ".txt");
                        }
                    }
                    logger.info("Total time for text extraction took " + (System.currentTimeMillis() - time) / 1000 + " seconds");

                }

            }
        } catch (JSAPException e) {
            throw new RuntimeException(e);
        }

    }

}
