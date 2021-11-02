package br.com.nex2you.api.controller.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import br.com.nex2you.api.factory.CustomElementFactoryImpl;

@Service
public class ConvertService {

	private static final String PDF_OUTPUT = "src/main/resources/pdf_test.pdf";

	@SuppressWarnings("deprecation")
	public void readfile(String documentId, Map<String, String> parameters) {
		try {
			StringBuffer buffer = new StringBuffer();
			FileReader fileReader = new FileReader("src/main/resources/".concat(documentId).concat(".html"));
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			Stream<String> lines = bufferedReader.lines();
			lines.forEach(line -> {
				buffer.append(line);
			});

			File file = File.createTempFile("index", "html");

			AtomicReference<String> stringFile = new AtomicReference<String>(buffer.toString());

			parameters.keySet().stream().forEach(key -> {
				stringFile.set(stringFile.get().replace("${".concat(key).concat("}"), parameters.get(key)));
			});

			FileUtils.write(file, stringFile.get());

			generateHtmlToPdf(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void generateHtmlToPdf(File file) throws Exception {
		// File fie = new File(HTML_INPUT);
		Document inputHtml = createWellFormedHtml(file);
		File outputPdf = new File(PDF_OUTPUT);
		xhtmlToPdf(inputHtml, outputPdf);
	}

	private Document createWellFormedHtml(File inputHTML) throws IOException {
		Document document = Jsoup.parse(inputHTML, "UTF-8");
		document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
		return document;
	}

	private void xhtmlToPdf(Document xhtml, File outputPdf) throws Exception {
		try (OutputStream outputStream = new FileOutputStream(outputPdf)) {
			ITextRenderer renderer = new ITextRenderer();
			SharedContext sharedContext = renderer.getSharedContext();
			sharedContext.setPrint(true);
			sharedContext.setInteractive(false);
			sharedContext.setReplacedElementFactory(new CustomElementFactoryImpl());
			renderer.setDocumentFromString(xhtml.html());
			renderer.layout();
			renderer.createPDF(outputStream);
		}
	}
}
