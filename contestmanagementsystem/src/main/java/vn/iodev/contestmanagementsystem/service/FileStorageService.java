package vn.iodev.contestmanagementsystem.service;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import vn.iodev.contestmanagementsystem.model.CuocThi;
import vn.iodev.contestmanagementsystem.model.FileIO;
import vn.iodev.contestmanagementsystem.repository.FileIORepository;

@Service
public class FileStorageService {
    @Autowired
    private FileIORepository fileIORepository;
  
    public FileIO store(MultipartFile file, CuocThi cuocThi) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileIO fileIO = new FileIO(fileName, file.getContentType(), file.getBytes());
        fileIO.setCuocThi(cuocThi);
  
        return fileIORepository.save(fileIO);
    }
  
    public FileIO getFile(String id) {
        return fileIORepository.findById(id).get();
    }
    
    public Stream<FileIO> getAllFiles() {
        return fileIORepository.findAll().stream();
    }

    public Stream<FileIO> getAllFilesByCuocThiId(String cuocThiId) {
        return fileIORepository.findByCuocThiId(cuocThiId).stream();
    }
}
