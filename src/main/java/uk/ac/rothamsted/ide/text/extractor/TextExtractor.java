package uk.ac.rothamsted.ide.text.extractor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
public class TextExtractor {
    private static final Logger logger = LogManager.getLogger(TextExtractor.class);
    public String extractText(String fileName) {
        InputStream stream = null;        

        try {
            stream = Files.newInputStream(Paths.get(fileName));
            AutoDetectParser parser = new AutoDetectParser();
            BodyContentHandler handler = new BodyContentHandler(-1);
            Metadata metadata = new Metadata();
            parser.parse(stream, handler, metadata, new ParseContext());
            return handler.toString();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (stream != null)
                try {
                    stream.close();
                } catch (IOException e) {
                    logger.error("Error closing stream");
                }
        }
        return fileName;
    }
}