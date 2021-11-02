package br.com.nex2you.api.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.nex2you.api.controller.service.ConvertService;

@RestController
public class ConvertController {

	@Autowired
	private ConvertService convertService;
	
	@SuppressWarnings("rawtypes")
	@PostMapping(value = "/{documentId}")
	public ResponseEntity test(@PathVariable String documentId, @RequestBody Map<String, String> parameters) {
		try {
			convertService.readfile(documentId, parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok("ok");
	}

}
