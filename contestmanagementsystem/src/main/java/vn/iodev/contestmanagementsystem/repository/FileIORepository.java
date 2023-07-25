package vn.iodev.contestmanagementsystem.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iodev.contestmanagementsystem.model.FileIO;

@Repository
public interface FileIORepository extends JpaRepository<FileIO, String> {
    @Transactional
    List<FileIO> findByCuocThiId(String cuocThiId);
}
