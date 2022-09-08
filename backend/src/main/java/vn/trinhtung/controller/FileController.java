package vn.trinhtung.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/file")
public class FileController {
	@GetMapping(value = "/image/{path}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<?> getImage(@PathVariable String path) {
		try {
			return ResponseEntity
					.ok(Files.readAllBytes(Paths.get("image" + File.separator + path)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
