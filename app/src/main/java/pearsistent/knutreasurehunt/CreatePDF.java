package pearsistent.knutreasurehunt;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class CreatePDF extends AppCompatActivity {

    private OutputStream output;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private DatabaseReference mDatabase;

    private File pdfFolder;
    private File myFile;
    private ArrayList<File> fileList;
    private ArrayList<OutputStream> outputStreamArrayList;
    private int teamNum = 0;

    private String imagePath;
    private ArrayList<Team> teamList = null;
    private String teamName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createpdf);

        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://treasurehunt-5d55f.firebaseio.com/");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://treasurehunt-5d55f.appspot.com");
        fileList = new ArrayList<File>();
        outputStreamArrayList = new ArrayList<>();


        // Read from the database
        mDatabase.child("Team").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ///팀을 받아와야 한다..

                Log.d("DB", "Cheer up ");

                for (DataSnapshot teamSnapshot : dataSnapshot.getChildren()) {
                    Team currentTeam = teamSnapshot.getValue(Team.class);

                    createPDFPath(teamNum);
                    makePDF(currentTeam,teamNum);
                    uploadPDFFile(currentTeam.getTeamName(),teamNum);

                    teamNum++;
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DB", "Failed to read value.", error.toException());
            }
        });

    }

    public void makePDF(Team currentTeam, int i){
        Document document = new Document();

        //You should create pdf in listener
        try {

            document.open();
            //Step 2
            PdfWriter.getInstance(document, outputStreamArrayList.get(i));
            //Step 3
            document.open();
            //Step 4 add data to document
            PdfPTable table = new PdfPTable(2);
            //Convert int to string
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(currentTeam.getTeamPoint());
            String point = sb.toString();

            table.addCell(currentTeam.getTeamName());
            table.addCell("score : " + point);
            //point, 멤버 더하기
            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {

        }
    }


    public void createPDFPath(int i){
        pdfFolder = new File("sdcard/TreasureHunt_PDF");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i("PDF directory", "Pdf Directory created");
        }
        myFile = new File(pdfFolder, "Doc"+String.valueOf(i)+".pdf");
        try {
            output = new FileOutputStream(myFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        fileList.add(myFile);
        outputStreamArrayList.add(output);
    }

    private PdfPTable createTable(Team team) {
        PdfPTable table = new PdfPTable(8);
        for (int i = 0; i < 3; i++) {
            table.addCell(team.getTeamName());
            int point = team.getTeamPoint();
            //  table.addCell(point);
        }
        return table;
    }

    //Upload File to Firebase
    private void uploadPDFFile(String name,int i) {
        //여기에 ref를 팀마다 받아오고
        //팀 리스트 사이즈만큼 pdf 생성
        StorageReference childRef = storageRef.child(name + "/" + name + ".pdf");
        Uri objectURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", fileList.get(i));
        UploadTask uploadTask = childRef.putFile(objectURI);
        childRef.putFile(objectURI);
        Log.d("Upload pdf", "success");

    }

    private StorageReference findImageFile(String imageFileName) {

        if (teamName != null) {
            //can make a image file name
            StorageReference childRef = storageRef.child(teamName + "/" + imageFileName);
            return childRef;
        }

        return null;
    }
}