package vn.iodev.contestmanagementsystem.localservice.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.iodev.contestmanagementsystem.localservice.ThiSinhLocalService;
import vn.iodev.contestmanagementsystem.model.DoanThi;
import vn.iodev.contestmanagementsystem.model.ThiSinh;
import vn.iodev.contestmanagementsystem.repository.DoanThiRepository;
import vn.iodev.contestmanagementsystem.repository.ThiSinhRepository;

@Service
public class ThiSinhLocalServiceImpl implements ThiSinhLocalService{
	@Autowired
	private DoanThiRepository doanThiRepository;
	@Autowired
	private ThiSinhRepository thiSinhRepository;
	@Override
	public List<ThiSinh> findByToChucId(String toChucId, Pageable pageable) {
		List<DoanThi> doanThis = doanThiRepository.findByToChucId(toChucId);
		List<String> doanThiIds = new ArrayList<String>();
		if(doanThis != null) {
			for(DoanThi doanThi : doanThis) {
				if(!doanThiIds.contains(doanThi.getId())) {
					doanThiIds.add(doanThi.getId());
				}
			}
		}
		
		return thiSinhRepository.findByDoanThiIds(doanThiIds, pageable);
	}


}
