package com.common.util;

import java.awt.Image;


import com.sun.jimi.core.Jimi;
import com.sun.jimi.core.JimiUtils;

public class Thumbnail {

	public void createThumbnail(String orig, String thumb, int width)
			throws Exception {
		Image image = JimiUtils.getThumbnail(orig, 640, 480, Jimi.IN_MEMORY);
		Jimi.putImage(image, thumb);

//		BufferedImage sourceImage = ImageIO.read(new File(orig));
//
//		// 아래의 2번째 아규먼트는 이미지의 가로/세로의 비를 유지한다는 의미이다
//		Image thumbnail = sourceImage.getScaledInstance(width, -1,	Image.SCALE_AREA_AVERAGING);
//		BufferedImage bufferedThumbnail = new BufferedImage(thumbnail.getWidth(null), thumbnail.getHeight(null),
//				BufferedImage.TYPE_INT_RGB);
//		bufferedThumbnail.getGraphics().drawImage(thumbnail, 0, 0, null);
//
//		ImageIO.write(bufferedThumbnail, "jpeg", new File(thumb));
	}
}
