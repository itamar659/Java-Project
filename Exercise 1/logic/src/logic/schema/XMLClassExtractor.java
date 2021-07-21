package logic.schema;

import logic.algorithm.factory.FactoryResult;
import logic.algorithm.factory.SelectionFactory;
import logic.algorithm.genericEvolutionAlgorithm.Selection;
import logic.schema.generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

public class XMLClassExtractor {

    private ETTDescriptor ettDescriptor;

    public XMLClassExtractor() {

    }

    public void initializeJAXB(File xmlFile) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ETTDescriptor.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        this.ettDescriptor = (ETTDescriptor) jaxbUnmarshaller.unmarshal(xmlFile);
    }

    public Selection extractSelectionOperator() {
        ETTSelection ettSelection = ettDescriptor.getETTEvolutionEngine().getETTSelection();
        FactoryResult frSelection = SelectionFactory.createSelection(
                ettSelection.getType(), createConfiguration(ettSelection.getConfiguration()));

//        if (frSelection.isErrorOccurred()) {
//            return frSelection.getErrorMessage();
//        }

        return (Selection) frSelection.getReturnedFactoryObject();
    }

    private String[][] createConfiguration(List<String> configuration) {
        if (configuration.size() == 0) {
            return null;
        }

        String[] configs = configuration.get(0).split(",");
        String[][] parameters = new String[configs.length][];

        for (int i = 0; i < configs.length; i++) {
            parameters[i] = configs[i].split("=");
        }

        return parameters;
    }
}
