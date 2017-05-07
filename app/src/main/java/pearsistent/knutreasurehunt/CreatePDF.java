package pearsistent.knutreasurehunt;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    ////////////////////
    private ArrayList<Item> itemList;
    private int count=0;
    private ArrayList<ByteArrayOutputStream> streamList = new ArrayList<ByteArrayOutputStream>();


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

                    //current team itemlist setting
                    for(int i = 0 ; i < currentTeam.getItemList().size() ; i ++){

                        Log.i("Who!!",currentTeam.getTeamName());

                        if(currentTeam.getItemList().get(0).getName()!="null")
                            currentTeam.getItemList().get(i).setImageReference(findImageFile(currentTeam.getItemList().get(i).getName() +".jpg",currentTeam.getTeamName()));
                    }

                    //DownloadStorage(currentTeam);

                    createPDFPath(teamNum);
                    makePDF(currentTeam, teamNum);


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

    /*private void DownloadStorage(Team currentTeam) {
        File treasureHuntImage = getFile(currentTeam.getTeamName());
        gs://treasurehunt-5d55f.appspot.com/bogyuTeam
        StorageReference teamStorage = storageRef.child(currentTeam.getTeamName());
        storageRef.child(currentTeam.getTeamName()).getFile(treasureHuntImage).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                long transfer = taskSnapshot.getBytesTransferred();
                Log.i("BFFFFFFF",transfer+"  ");
            }
        });
    }
*/
    public void makePDF(final Team currentTeam, final int i) {

        Log.i("MAKEPDF",currentTeam.getTeamName());
        final Document document = new Document();

        try {
            document.open();
            //Step 2
            PdfWriter.getInstance(document, outputStreamArrayList.get(i));
            //Step 3
            document.open();
            //Step 4 add data to document
            /////////////for Team
            final PdfPTable table = new PdfPTable(2);

            //Convert int to string
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(currentTeam.getTeamPoint());
            String point = sb.toString();

            table.addCell(currentTeam.getTeamName());
            table.addCell("score : " + point);

            document.add(table);

            addImageToPDF(currentTeam,document,i);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    public void addImageToPDF(final Team currentTeam, final Document document, final int index){

        Log.i("AddImageToPDF","INTO!");
        final ArrayList<File> FileList = new ArrayList<File>();
        int itemCount=0;

        for (int item_count = 0; item_count < currentTeam.getItemList().size(); item_count++) {
            final File tempfile = getFile(count++);
            itemCount++;
            final int temp = itemCount;

            final StorageReference islandRef = storageRef.child(currentTeam.getTeamName()).child(currentTeam.getItemList().get(item_count).getName()+".jpg");
            Log.d("ItemList",currentTeam.getTeamName()+"  "+ currentTeam.getItemList().get(item_count).getName());

            islandRef.getFile(tempfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    FileList.add(tempfile);
                    Log.d("makeFile", "Success");
                    Log.d("FileListSize", "" + FileList.size());
                    String path = tempfile.getPath();
                    Log.d("FilePath", "" + path);

                    try {

                        Image image = Image.getInstance(tempfile.getPath());
                        //image.setAbsolutePosition(5,5);
                        document.add(image);

                        if(temp == currentTeam.getItemList().size()){

                            document.close();
                            uploadPDFFile(currentTeam.getTeamName(),index);
                        }

                    }  catch (IOException e) {
                        Log.i("Errrrrrr","InputStream");
                        e.printStackTrace();
                    } catch (BadElementException e) {
                        e.printStackTrace();
                        Log.i("Errrrrrr","Image");
                    } catch (DocumentException e) {
                        Log.i("Errrrrrr","Document");
                        e.printStackTrace();
                    }
                }
            });
        }


        Log.i("AddImageToPDF","FINISH!");

    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByte, 0,    decodedByte.length);
    }
    public void createPDFPath(int i) {
        pdfFolder = new File("sdcard/TreasureHunt_PDF");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i("PDF directory", "Pdf Directory created");
        }
        myFile = new File(pdfFolder, "Doc" + String.valueOf(i) + ".pdf");
        try {
            output = new FileOutputStream(myFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        fileList.add(myFile);
        outputStreamArrayList.add(output);

    }

    //Upload File to Firebase
    private void uploadPDFFile(String name, int i) {
        //여기에 ref를 팀마다 받아오고
        //팀 리스트 사이즈만큼 pdf 생성
        StorageReference childRef = storageRef.child(name + "/" + name + ".pdf");
        Uri objectURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", fileList.get(i));
        UploadTask uploadTask = childRef.putFile(objectURI);
        childRef.putFile(objectURI);
        Log.d("Upload pdf", "success");
    }

    private StorageReference findImageFile(String imageFileName, String teamName) {

        if (teamName != null) {
            //can make a image file name
            StorageReference childRef = storageRef.child(teamName + "/" + imageFileName);
            return childRef;
        }

        return null;
    }

    private File getFile(int num) {

        File folder = new File("sdcard/pdf_images");

        if (!folder.exists()) {
            folder.mkdir();
        }

        //dynamically make a file name
        File imageFile = new File(folder, "Image"+num+".jpg");

        if (imageFile.exists()) {
            imageFile.delete();
            imageFile = new File(folder, "Image"+num+".jpg");
        }

        return imageFile;
    }

    private Uri getFileUri(File fileName) {

        Uri objectURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", fileName);

        return objectURI;
    }

}