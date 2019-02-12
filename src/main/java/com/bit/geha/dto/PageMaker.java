package com.bit.geha.dto;

import lombok.Data;

//게시판 페이징 하단 부문 담당
@Data
public class PageMaker {
	private int totalCount;	//게시판 전체 데이터 개수
	private int displayPageNum;		//게시판 화면에서 한번에 보여질 페이지 번호의 개수 (1,2,3,4,5,6,7,8,9,10)
	
	private int startPage;	//현재 화면에서 보이는 startPage 번호
	private int endPage;	//현재 화면에서 보이는 endPage 번호
	private boolean prev;	//페이징 이전 버튼 활성화 여부
	private boolean next;	//페이징 다음 버튼 활성화 여부
	
	private BoardCriteria cri;	//앞서 생성한 BoardCriteria를 주입받는다.
	
	public int getTotalCount() {
		return totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		
		calcData();
	}
	
	private void calcData() {
		endPage = (int)(Math.ceil(cri.getPage() / (double) displayPageNum) * displayPageNum);
		
		startPage = (endPage - displayPageNum) + 1;
		
		int tempEndPage = (int)(Math.ceil(totalCount / (double) cri.getPerPageNum()));
		
		if(endPage > tempEndPage) {
			endPage = tempEndPage;
		}
		
		prev = startPage == 1 ? false : true;
		
		next = endPage * cri.getPerPageNum() > totalCount ? false : true;
	}
	
	@Override
	public String toString() {
		return "PageMaker [totalCount=" + totalCount + ", startPage=" + startPage + ", endPage=" + endPage
				+ ", prev=" + prev + ", next=" + next + ", displayNum=" + displayPageNum + ", cri=" + cri + "]";
	}
}
