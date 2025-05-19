package me.khs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.khs.dto.ReceiptDto;
import me.khs.entity.ReceiptEntity;
import me.khs.repository.ReceiptRepository;

@Service
public class ReceiptService {

	@Autowired
	private ReceiptRepository receiptRepository;
	
	public ReceiptService(ReceiptRepository receiptRepository) {
		
		this.receiptRepository = receiptRepository;
		
	}
	
	public void createReceipt (ReceiptDto receiptDto) {
		
		ReceiptEntity receipt = ReceiptEntity.builder()
				.shop(receiptDto.getShop())
				.userId(receiptDto.getUserId())
				.date(receiptDto.getDate())
				.keywordId(receiptDto.getKeywordId())
				.build();
		
		receiptRepository.save(receipt);
		
	}
	
}
