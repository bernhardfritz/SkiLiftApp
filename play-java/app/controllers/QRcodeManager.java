package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;


public class QRcodeManager {

	private static HashManager hash;

	public QRcodeManager(HashManager h){ //Constructor
		QRcodeManager.setHash(h);
	}

	private static class SingletonHelper {
		private static final QRcodeManager INSTANCE = new QRcodeManager(hash);
	}

	public static QRcodeManager getInstance() {
		return SingletonHelper.INSTANCE;

	}
	
	public String generateHash(QRcodeData data){ //creates a Hash with a String
		String DataString = data.InfoToString();
		getHash();
		String codeString = HashManager.codeString(DataString);
		return codeString;
}

	

	public void generateQRcode(String codeString) { // creates a qrcode.png with a Hash
		try{
		QRCode.from(codeString).to(ImageType.PNG)
				.writeTo(new FileOutputStream(new File("qrcode.png"))); // Stream implementation fehlt
		} catch (FileNotFoundException e) {
			System.out.println("File qrcode.png not found");
			e.printStackTrace();
		}
	}

	public static HashManager getHash() {
		return hash;
	}

	public static void setHash(HashManager hash) {
		QRcodeManager.hash = hash;
	}
	
	public static String readQRCode(String filePath, String charset, Map hintMap){ //decodes a given qr code (filepath, "UTF-8", new HashMap(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		BinaryBitmap binaryBitmap = null;
		try {
			binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath)))));
		} catch (IOException e) {
			System.out.println("File "+filePath+" not Found");
			e.printStackTrace();
		}
		Result qrCodeResult = null;
		try {
			qrCodeResult = new MultiFormatReader().decode(binaryBitmap,hintMap);
		} catch (NotFoundException e) {
			System.out.println("File "+filePath+" not Found");
			e.printStackTrace();
		}
		return qrCodeResult.toString();
	}
}
