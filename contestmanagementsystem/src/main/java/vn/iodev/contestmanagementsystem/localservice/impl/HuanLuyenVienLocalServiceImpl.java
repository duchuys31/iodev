package vn.iodev.contestmanagementsystem.localservice.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import vn.iodev.contestmanagementsystem.localservice.HuanLuyenVienLocalService;
import vn.iodev.contestmanagementsystem.model.DoanThi;
import vn.iodev.contestmanagementsystem.model.HuanLuyenVien;
import vn.iodev.contestmanagementsystem.repository.DoanThiRepository;
import vn.iodev.contestmanagementsystem.repository.HuanLuyenVienRepository;

@Service
@Slf4j
public class HuanLuyenVienLocalServiceImpl implements HuanLuyenVienLocalService{
	@Autowired
	private DoanThiRepository doanThiRepository;
	
	@Autowired
	private HuanLuyenVienRepository huanLuyenVienRepository;
	
	@Override
	public List<HuanLuyenVien> findByToChucId(String toChucId, Pageable pageable) {
		List<DoanThi> doanThis = doanThiRepository.findByToChucId(toChucId);
		List<String> doanThiIds = new ArrayList<String>();
		if(doanThis != null) {
			for(DoanThi doanThi : doanThis) {
				if(!doanThiIds.contains(doanThi.getId())) {
					doanThiIds.add(doanThi.getId());
				}
			}
		}
		log.info("doanThiIds list size", doanThiIds.size());
		return huanLuyenVienRepository.findByDoanThiIds(doanThiIds, pageable);
	}

}
