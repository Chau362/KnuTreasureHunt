package pearsistent.knutreasurehunt;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class CreatePDF extends AppCompatActivity {

    private OutputStream output;

    private File pdfFolder;
    private File myFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createpdf);

        Log.d("CreatePDF","success open");
        try {
            createPath();
            createPDF();
            Toast.makeText(this, "Success!",
                    Toast.LENGTH_SHORT).show();
            //viewPdf();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void createPath() throws FileNotFoundException {

        pdfFolder = new File("sdcard/TreasureHunt_PDF");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i("PDF directory", "Pdf Directory created");
        }
        myFile = new File(pdfFolder, "Doc1.pdf");
        output = new FileOutputStream(myFile);
    }

    // @TargetApi(Build.VERSION_CODES.KITKAT)
    public void createPDF() throws DocumentException {
        //Step 1
        Document document = new Document();

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.open();

        //Step 4 Add content
//        document.add(new Paragraph(mSubjectEditText.getText().toString()));
//        document.add(new Paragraph(mBodyEditText.getText().toString()));

        document.add(new Paragraph("test"));
        document.add(new Paragraph("Testttt"));

        //Step 5: Close the document
        document.close();
    }

}