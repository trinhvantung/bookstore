package vn.trinhtung.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import net.bytebuddy.utility.RandomString;

@Component
public class FileUtil {
	public String upload(MultipartFile file, String dir) {
		Path path = Paths.get(dir);
		String fileName = RandomString.make(32) + file.getOriginalFilename();

		try {
			if (!Files.exists(path)) {
				Files.createDirectories(path);
			}
			Path pathFile = path.resolve(fileName);

			Files.copy(file.getInputStream(), pathFile, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dir + "/" + fileName;
	}

	public void delete(String filePath) {
		Path path = Paths.get(filePath);
		try {
			Files.delete(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
