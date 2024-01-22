package com.eimsky.parse.v01.storage;

import com.eimsky.parse.v01.dto.RFID.RFID_DTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {
	void init();
	String store(MultipartFile file);
	Stream<Path> loadAll();
	Path load(String filename);
	Resource loadAsResource(String filename);
	void cleanDir();
}
