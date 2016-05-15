package textanalysis.parser;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.languagetool.AnalyzedSentence;
import org.languagetool.JLanguageTool;
import textanalysis.Main;

public class FrequencyCalculator {

    private JLanguageTool jlt;

    class Frequency {

        HashSet<String> documents = new HashSet<>();
        public int d = 0;
        public int c = 0;
    }

    private HashMap<String, Frequency> inverseIndex = new HashMap<>();
    private HashMap<String, Double> inverseDocumentFrequency = new HashMap<>();
    private HashMap<String, Double> termFrequency = new HashMap<>();

    public FrequencyCalculator(JLanguageTool lt) {
        this.jlt = lt;
    }

    public FrequencyCalculator(String path) {
        this.loadFromJson(path);
    }

    public HashMap<String, Double> calculateTf(List<AnalyzedSentence> sentences) {

        this.termFrequency.clear();

        int words = 0;

        for (AnalyzedSentence it : sentences) {
            for (String lemma : it.getLemmaSet()) {
                String trimmed = lemma.trim();
                if (trimmed != "") {
                    words++;
                    this.termFrequency.put(trimmed, this.termFrequency.getOrDefault(trimmed, 0.0) + 1);
                }
            }
        }

        for (Entry<String, Double> it : this.termFrequency.entrySet()) {
            it.setValue(it.getValue() / words);
        }

        return this.termFrequency;

    }

    void calculateIdf(String path) throws IOException {

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        int filesCount = 0;

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {

                byte[] encoded = Files.readAllBytes(Paths.get(listOfFiles[i].getPath()));
                String fileStr = new String(encoded, StandardCharsets.UTF_8);

                List<AnalyzedSentence> sentences = this.jlt.analyzeText(fileStr);
                for (AnalyzedSentence it : sentences) {
                    for (String lemma : it.getLemmaSet()) {

                        String trimmed = lemma.trim();

                        Frequency termFrequency = this.inverseIndex.getOrDefault(trimmed, new Frequency());
                        termFrequency.documents.add(listOfFiles[i].getName());
                        termFrequency.c++;

                        this.inverseIndex.put(trimmed, termFrequency);
                    }
                }
            }
        }

        for (Entry<String, Frequency> it : this.inverseIndex.entrySet()) {
            it.getValue().d = it.getValue().documents.size();
//            it.getValue().idf = Math.log(4499/it.getValue().d);
        }

        ObjectMapper om = new ObjectMapper();
        try {
            System.out.println(om.writeValueAsString(inverseIndex));

        } catch (JsonProcessingException ex) {
            Logger.getLogger(Main.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("files in dir:" + filesCount);

    }

    protected void loadFromJson(String path) {

        try {

            JsonFactory factory = new JsonFactory();

            ObjectMapper mapper = new ObjectMapper(factory);
            JsonNode rootNode = mapper.readTree(new File(path));

            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();
            while (fieldsIterator.hasNext()) {

                Map.Entry<String, JsonNode> field = fieldsIterator.next();
                this.inverseDocumentFrequency.put(field.getKey(), Math.log(4499 / field.getValue().get("d").asDouble()));
            }
        } catch (IOException ex) {
            Logger.getLogger(FrequencyCalculator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public double calculateTfIdf(String word) {
        
        double defaultCoeff = 0.0001;
        
        if (word.length() < 3) {
            return defaultCoeff;
        }

        word = word.trim();
        return this.termFrequency.getOrDefault(word, defaultCoeff) * this.inverseDocumentFrequency.getOrDefault(word, defaultCoeff);
    }

}
