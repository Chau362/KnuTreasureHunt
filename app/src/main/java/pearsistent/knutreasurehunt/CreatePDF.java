package pearsistent.knutreasurehunt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    private String path;
    private  File pdfFolder;
    private EditText mSubjectEditText, mBodyEditText;
    private Button mSaveButton;
    private File myFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createpdf);
        mSubjectEditText = (EditText) this.findViewById(R.id.edit_text_subject);
        mBodyEditText = (EditText) this.findViewById(R.id.edit_text_body);
        mSaveButton = (Button) this.findViewById(R.id.button_save);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSubjectEditText.getText().toString().isEmpty()){
                    mSubjectEditText.setError("Subject is empty");
                    mSubjectEditText.requestFocus();
                    return;
                }

                if (mBodyEditText.getText().toString().isEmpty()){
                    mBodyEditText.setError("Body is empty");
                    mBodyEditText.requestFocus();
                    return;
                }

                try {
                    createPath();
                    createPDF();

                    //viewPdf();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void viewPdf(){
        Intent intent = new Intent(Intent.ACTION_VIEW);

        Uri objectURI = FileProvider.getUriForFile(getApplicationContext(),getApplicationContext().getPackageName()+".provider",myFile);
        intent.setDataAndType(objectURI, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void createPath() throws FileNotFoundException{

        pdfFolder = new File("sdcard/TreasureHunt_PDF");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i("PDF directory", "Pdf Directory created");
        }

        //Create time stamp
        //Date date = new Date();
        ///String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
        myFile = new File(pdfFolder,"Doc1.pdf");
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
        document.add(new Paragraph(mSubjectEditText.getText().toString()));
        document.add(new Paragraph(mBodyEditText.getText().toString()));

        //Step 5: Close the document
        document.close();
    }

}