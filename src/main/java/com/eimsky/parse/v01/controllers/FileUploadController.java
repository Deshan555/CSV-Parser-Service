package com.eimsky.parse.v01.controllers;

import com.eimsky.parse.v01.dto.RFID.RFID_DTO;
import com.eimsky.parse.v01.dto.RFID.RFID_DTO_Response;
import com.eimsky.parse.v01.dto.RFID.RFID_Validator;
import com.eimsky.parse.v01.dto.RFID.RFID_Validator_Response;
import com.eimsky.parse.v01.exceptions.StorageFileNotFoundException;
import com.eimsky.parse.v01.json.Json;
import com.eimsky.parse.v01.json.Response;
import com.eimsky.parse.v01.storage.StorageService;
import com.eimsky.parse.v01.services.TransformerFacade;
import com.eimsky.parse.v01.services.facades.MigrationFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("api.eimsky.com/parse/v01")
public class FileUploadController {
	private final StorageService storageService;
	private final TransformerFacade transformerFacade;
	private final MigrationFacade migrationFacade;
	@Autowired
	public FileUploadController(StorageService storageService, TransformerFacade transformerFacade, MigrationFacade migrationFacade) {
		this.storageService = storageService;
		this.transformerFacade = transformerFacade;
		this.migrationFacade = migrationFacade;
	}

	@GetMapping("/")
	public void listUploadedFiles(Model model) throws IOException {

		model.addAttribute("files", storageService.loadAll().map(
				path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
						"serveFile", path.getFileName().toString()).build().toUri().toString())
				.collect(Collectors.toList()));
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);

		if (file == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	/*@PostMapping("/")
	public String handleFileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {
		storageService.store(file);
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		return "redirect:/";
	}*/

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

	@PostMapping("/upload/rfid")
	public void handleRFIDUpload(@ModelAttribute RFID_DTO rfidDTO,
								   RedirectAttributes redirectAttributes) {
		storageService.store(rfidDTO.getFile());
		//System.out.println(rfidDTO.getCardStatus());
		//System.out.println(rfidDTO.getUserID());

		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + rfidDTO.getFile().getOriginalFilename() + "!");
	}

	/*@PostMapping("/validate/rfid")
	public ResponseEntity<?> validateDataTransform(@RequestBody RFID_Validator rfidValidator){
		Boolean responseData = transformerFacade.checkAndTransform_RFID(rfidValidator);
		System.out.println("Transformer Said : "+responseData);
		return ResponseEntity.ok(responseData);
	}*/

	@PostMapping("/attendance/migrate/import")
	public ResponseEntity<Json> validateDataTransform(@ModelAttribute RFID_DTO rfidDto){
		String filePath = storageService.store(rfidDto.getFile());
		Json newJson = new Json();
		if(filePath == null){
			Response response = new Response(Response.ERROR,
					"RFID Data Import Failed", null);
			newJson.setResponse(response);
			log.warn("FileUploadController.validateDataTransform() - error: {}", "File Upload Failed");
			return new ResponseEntity<>(newJson, HttpStatus.BAD_REQUEST);
		}else{
			try {
				RFID_DTO_Response Rfid_DTO_Response = migrationFacade.checkAndImport_RFID(filePath, rfidDto);
				Response response = new Response(Response.SUCCESS,
						"RFID Data Imported Successfully", Rfid_DTO_Response);
				newJson.setResponse(response);
				log.info("FileUploadController.validateDataTransform() - response: {}", response);
				return new ResponseEntity<>(newJson, HttpStatus.OK);
			}catch(Exception Error){
				Response response = new Response(Response.ERROR,
						"RFID Data Import Failed", null);
				newJson.setResponse(response);
				log.warn("FileUploadController.validateDataTransform() - error: {}", Error.getMessage());
				return new ResponseEntity<>(newJson, HttpStatus.BAD_REQUEST);
			}
		}
	}

	@PostMapping("/attendance/migrate/validate")
	public ResponseEntity<Json> migrateDataTransform(@RequestBody RFID_Validator rfidValidator){
		Json newJson = new Json();
		try{
			RFID_Validator_Response Rfid_Validator_Response = migrationFacade.checkAndTransform_RFID(rfidValidator);
			Response response = new Response(Response.SUCCESS,
					"RFID Data Migrated Successfully", Rfid_Validator_Response);
			newJson.setResponse(response);
			log.info("FileUploadController.migrateDataTransform() - response: {}", response);
			return new ResponseEntity<>(newJson, HttpStatus.OK);
		}catch(Exception Error){
			Response response = new Response(Response.ERROR,
					"RFID Data Migration Failed", null);
			newJson.setResponse(response);
			log.warn("FileUploadController.migrateDataTransform() - error: {}", Error.getMessage());
			return new ResponseEntity<>(newJson, HttpStatus.BAD_REQUEST);
		}
	}

}
