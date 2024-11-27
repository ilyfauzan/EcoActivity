package com.example.ecoactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnBack;
    private TextView tvPredictionTitle;
    private TextView tvPersonalizedMessage;
    private ImageView imgNothing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        recyclerView = findViewById(R.id.recyclerView);
        btnBack = findViewById(R.id.btnBack);
        tvPredictionTitle = findViewById(R.id.tvPredictionTitle);
        tvPersonalizedMessage = findViewById(R.id.tvPersonalizedMessage);
        imgNothing = findViewById(R.id.imgNothing);

        // Ambil data dari Intent
        String prediction = getIntent().getStringExtra("prediction");
        int age = getIntent().getIntExtra("age", 0); // Ambil usia dari Intent
        String name = getIntent().getStringExtra("name");
        String kondisi = getIntent().getStringExtra("kondisi");

        btnBack.setOnClickListener(v -> onBackPressed());

        // Jika kondisi adalah "Sakit", tampilkan pesan istirahat
        if ("Sakit".equalsIgnoreCase(kondisi)) {
            tvPredictionTitle.setText("Tidak Ada Rekomendasi");
            tvPersonalizedMessage.setText("Hai " + name + ", karena Anda sedang sakit, disarankan untuk beristirahat. Semoga cepat sembuh!");
            recyclerView.setVisibility(View.GONE);
            btnBack.setVisibility(View.VISIBLE);
            imgNothing.setVisibility(View.VISIBLE);// Hide RecyclerView because no activities will be shown
        } else {
            // Display activity recommendations based on the prediction
            if ("indoor".equalsIgnoreCase(prediction)) {
                tvPredictionTitle.setText("Indoor Activities");
            } else if ("outdoor".equalsIgnoreCase(prediction)) {
                tvPredictionTitle.setText("Outdoor Activities");
            } else {
                tvPredictionTitle.setText("Activities");
                imgNothing.setVisibility(View.GONE);
            }

            String activityType = prediction.equalsIgnoreCase("indoor") ? "di dalam ruangan" : "di luar ruangan";
            String personalizedMessage = "Hai " + name + ", kami memiliki 5 rekomendasi aktivitas " + activityType + " untuk Anda. Selamat bersenang-senang!";
            tvPersonalizedMessage.setText(personalizedMessage);

            // Daftar kegiatan berdasarkan prediksi
            List<ActivityItem> activityList = new ArrayList<>();

            // Jika usia di bawah 15 tahun, hanya tampilkan aktivitas yang cocok untuk anak-anak
            if (age < 15) {
                // Aktivitas indoor untuk anak-anak
                if ("indoor".equalsIgnoreCase(prediction)) {
                    activityList.add(new ActivityItem("Bermain Game", "Bersantai dengan bermain game favorit.", R.drawable.gaming));
                    activityList.add(new ActivityItem("Menonton Film", "Nikmati waktu santai dengan film favorit.", R.drawable.watching));
                    activityList.add(new ActivityItem("Membaca Buku", "Luangkan waktu untuk meningkatkan wawasan.", R.drawable.reading));
                    activityList.add(new ActivityItem("Menyusun Lego", "Kembangkan kreativitas dengan menyusun lego.", R.drawable.lego));
                    activityList.add(new ActivityItem("Bermain Puzzle", "Latih otak Anda dengan menyusun puzzle.", R.drawable.puzzle));
                    activityList.add(new ActivityItem("Bermain Musik", "Belajar bermain alat musik seperti piano atau gitar.", R.drawable.music));
                    activityList.add(new ActivityItem("Melukis atau Mewarnai", "Ekspresikan kreativitas dengan melukis atau mewarnai gambar.", R.drawable.draw));
                    activityList.add(new ActivityItem("Bermain Boneka", "Ceritakan kisah seru dengan boneka favorit.", R.drawable.doll));
                    activityList.add(new ActivityItem("Membuat Origami", "Kreasi seni melipat kertas menjadi bentuk yang menarik.", R.drawable.ori));
                    activityList.add(new ActivityItem("Membangun Kota dengan Balok", "Gunakan balok mainan untuk membangun sesuatu yang baru.", R.drawable.blok));
                    activityList.add(new ActivityItem("Eksperimen Sains Mini", "Lakukan eksperimen sederhana seperti membuat gunung meletus dengan baking soda.", R.drawable.eating));
                    activityList.add(new ActivityItem("Bermain Tebak Kata", "Permainan menyenangkan untuk melatih kosa kata.", R.drawable.play));
                    activityList.add(new ActivityItem("Bermain Peran", "Bermain menjadi karakter favorit seperti dokter atau superhero.", R.drawable.role));
                    activityList.add(new ActivityItem("Berolahraga di Rumah", "Melakukan olahraga ringan seperti lompat tali atau yoga untuk anak-anak.", R.drawable.ic_home_exercise));
                    activityList.add(new ActivityItem("Membuat Kerajinan Tangan", "Kreasikan sesuatu dengan kertas, gunting, dan lem.", R.drawable.ic_crafting));
                    activityList.add(new ActivityItem("Mendengarkan Dongeng", "Nikmati cerita menarik dari buku cerita atau audio book.", R.drawable.ic_story_telling));
                    activityList.add(new ActivityItem("Belajar Menanam Tanaman", "Tanam dan rawat tanaman kecil seperti bunga atau sayuran.", R.drawable.ic_planting));
                    activityList.add(new ActivityItem("Bermain Mainan Puzzle 3D", "Latih logika dengan menyusun puzzle yang menantang.", R.drawable.puzzle));
                    activityList.add(new ActivityItem("Belajar Coding untuk Anak", "Cobalah aplikasi coding sederhana yang dirancang untuk anak-anak.", R.drawable.ic_coding));
                    activityList.add(new ActivityItem("Bermain Petak Umpet", "Aktivitas seru yang bisa dilakukan di dalam rumah.", R.drawable.ic_hide_and_seek));
                    activityList.add(new ActivityItem("Membuat Buku Cerita Sendiri", "Ciptakan cerita sendiri dan ilustrasikan dengan gambar.", R.drawable.reading));
                    activityList.add(new ActivityItem("Membangun Menara dengan Kartu", "Gunakan kartu untuk membangun menara tinggi.", R.drawable.ic_crafting));
                    activityList.add(new ActivityItem("Belajar Menyanyi", "Nyanyikan lagu favorit dan pelajari nada-nada baru.", R.drawable.music));
                    activityList.add(new ActivityItem("Membuat Video Kreatif", "Buat video pendek tentang kehidupan sehari-hari atau cerita imajinasi.", R.drawable.ic_creative_video));

                }
                // Aktivitas outdoor untuk anak-anak
                else if ("outdoor".equalsIgnoreCase(prediction)) {
                    activityList.add(new ActivityItem("Bermain Sepak Bola", "Aktivitas olahraga bersama teman.", R.drawable.ball));
                    activityList.add(new ActivityItem("Bermain Layang-Layang", "Nikmati angin di taman dengan bermain layang-layang.", R.drawable.kite));
                    activityList.add(new ActivityItem("Bermain Frisbee", "Bersenang-senang dengan frisbee di taman.", R.drawable.kite));
                    activityList.add(new ActivityItem("Piknik", "Habiskan waktu bersama keluarga di taman.", R.drawable.picnic));
                    activityList.add(new ActivityItem("Bermain di Taman Bermain", "Waktu menyenangkan untuk anak-anak dan keluarga.", R.drawable.garden));
                    activityList.add(new ActivityItem("Mendaki Bukit", "Jelajahi bukit kecil di sekitar lingkungan Anda.", R.drawable.hill));
                    activityList.add(new ActivityItem("Bermain Air", "Main air di kolam renang atau taman air.", R.drawable.swim));
                    activityList.add(new ActivityItem("Mengamati Burung", "Gunakan teropong untuk melihat burung di taman.", R.drawable.bird));
                    activityList.add(new ActivityItem("Bersepeda", "Nikmati udara segar dengan bersepeda di sekitar lingkungan.", R.drawable.bicycle));
                    activityList.add(new ActivityItem("Bermain Pasir", "Bangun istana pasir di pantai atau taman bermain.", R.drawable.blok));
                    activityList.add(new ActivityItem("Bermain Bola Keranjang", "Nikmati permainan bola keranjang bersama teman-teman.", R.drawable.ball));
                    activityList.add(new ActivityItem("Bermain Petak Umpet", "Permainan seru yang bisa dilakukan di taman.", R.drawable.ic_hide_and_seek));
                    activityList.add(new ActivityItem("Bermain Tenda-Tendaan", "Dirikan tenda kecil untuk bermain atau piknik di taman.", R.drawable.tent));
                    activityList.add(new ActivityItem("Bermain Lompat Tali", "Aktivitas menyenangkan untuk melatih kelincahan.", R.drawable.rope));
                    activityList.add(new ActivityItem("Bermain Tangkap Bola", "Latihan refleks dengan menangkap bola yang dilempar.", R.drawable.ball));
                    activityList.add(new ActivityItem("Bermain dengan Gelembung Sabun", "Tiup dan tangkap gelembung sabun di udara.", R.drawable.soap));
                    activityList.add(new ActivityItem("Mengikuti Lomba Lari", "Ikut serta dalam lomba lari santai bersama teman-teman.", R.drawable.running));
                    activityList.add(new ActivityItem("Bermain Kelereng", "Permainan tradisional yang menyenangkan di luar rumah.", R.drawable.marbles));
                    activityList.add(new ActivityItem("Bermain di Kolam Ikan", "Berinteraksi dengan ikan kecil di kolam taman.", R.drawable.fish));
                    activityList.add(new ActivityItem("Berburu Harta Karun", "Ciptakan petualangan berburu harta karun di taman.", R.drawable.trea));
                }

                Collections.shuffle(activityList);

                // Ambil 5 aktivitas pertama
                List<ActivityItem> randomActivities = activityList.subList(0, Math.min(5, activityList.size()));

            } else {
                // Jika usia lebih dari atau sama dengan 15 tahun, tampilkan semua aktivitas yang ada
                if ("indoor".equalsIgnoreCase(prediction)) {
                    activityList.add(new ActivityItem("Yoga", "Meningkatkan fleksibilitas dan relaksasi.", R.drawable.yoga));
                    activityList.add(new ActivityItem("Meditasi", "Menenangkan pikiran dan tubuh.", R.drawable.medi));
                    activityList.add(new ActivityItem("Menonton Film", "Nikmati waktu santai dengan film favorit.", R.drawable.watching));
                    activityList.add(new ActivityItem("Membaca Buku", "Luangkan waktu untuk meningkatkan wawasan.", R.drawable.reading));
                    activityList.add(new ActivityItem("Belajar Memasak", "Cobalah resep baru dan eksplorasi rasa.", R.drawable.cook));
                    activityList.add(new ActivityItem("Mendengarkan Musik", "Relaksasi dengan lagu favorit.", R.drawable.music));
                    activityList.add(new ActivityItem("Melukis", "Ekspresikan kreativitas Anda melalui seni.", R.drawable.draw));
                    activityList.add(new ActivityItem("Menulis Jurnal", "Luangkan waktu untuk menulis pengalaman Anda.", R.drawable.draw));
                    activityList.add(new ActivityItem("Bermain Game", "Bersantai dengan bermain game favorit.", R.drawable.gaming));
                    activityList.add(new ActivityItem("Menjahit atau Merajut", "Cobalah proyek DIY yang menyenangkan.", R.drawable.ic_crafting));
                    activityList.add(new ActivityItem("Membuat Kerajinan", "Ekspresikan kreativitas Anda dengan kerajinan tangan.", R.drawable.ic_crafting));
                    activityList.add(new ActivityItem("Bermain Piano", "Belajar atau memainkan musik klasik.", R.drawable.piano));
                    activityList.add(new ActivityItem("Menonton Serial", "Habiskan waktu dengan serial favorit Anda.", R.drawable.watching));
                    activityList.add(new ActivityItem("Membaca Novel", "Nikmati cerita baru dengan membaca novel.", R.drawable.reading));
                    activityList.add(new ActivityItem("Bersih-Bersih Rumah", "Ciptakan suasana bersih dan nyaman.", R.drawable.home));
                    activityList.add(new ActivityItem("Latihan Fisik Ringan", "Tetap aktif dengan olahraga ringan di rumah.", R.drawable.ic_home_exercise));
                    activityList.add(new ActivityItem("Belajar Online", "Ikuti kursus online untuk pengembangan diri.", R.drawable.ic_story_telling));
                    activityList.add(new ActivityItem("Membuat Scrapbook", "Buat album kenangan dengan foto-foto Anda.", R.drawable.ic_story_telling));
                    activityList.add(new ActivityItem("Bermain Puzzle", "Latih otak Anda dengan menyusun puzzle.", R.drawable.puzzle));
                    activityList.add(new ActivityItem("Merawat Tanaman Indoor", "Ciptakan ruang hijau di dalam rumah.", R.drawable.ic_planting));
                    activityList.add(new ActivityItem("Membuat Video", "Ekspresikan kreativitas Anda dengan membuat vlog.", R.drawable.ic_creative_video));
                    activityList.add(new ActivityItem("Bermain dengan Hewan Peliharaan", "Nikmati waktu bersama teman berbulu Anda.", R.drawable.fish));
                    activityList.add(new ActivityItem("Belajar Bahasa Baru", "Tambah wawasan dengan mempelajari bahasa asing.", R.drawable.ic_story_telling));
                    activityList.add(new ActivityItem("Membuat Kue", "Cobalah membuat dessert manis favorit Anda.", R.drawable.cook));
                    activityList.add(new ActivityItem("Belajar Origami", "Eksplorasi seni melipat kertas menjadi karya indah.", R.drawable.ori));
                    activityList.add(new ActivityItem("Kelas Online Yoga", "Ikuti kelas yoga online dari kenyamanan rumah.", R.drawable.yoga));
                    activityList.add(new ActivityItem("Bermain Catur", "Tingkatkan strategi Anda dengan bermain catur.", R.drawable.play));
                    activityList.add(new ActivityItem("Latihan Tari", "Bersenang-senang dengan mencoba tarian baru.", R.drawable.yoga));
                    activityList.add(new ActivityItem("Menyusun Lego", "Kembangkan kreativitas dengan menyusun lego.", R.drawable.lego));
                    activityList.add(new ActivityItem("Menonton Dokumenter", "Tingkatkan wawasan dengan menonton dokumenter.", R.drawable.ic_creative_video));
                    activityList.add(new ActivityItem("Belajar Makeup", "Eksplorasi gaya baru dengan belajar makeup.", R.drawable.make));
                    activityList.add(new ActivityItem("Eksperimen Sains Mini", "Coba eksperimen sederhana dengan bahan di rumah.", R.drawable.ic_crafting));
                    activityList.add(new ActivityItem("Latihan Kaligrafi", "Pelajari seni menulis indah dengan kaligrafi.", R.drawable.kali));
                    activityList.add(new ActivityItem("Latihan Piano Virtual", "Gunakan aplikasi piano untuk belajar lagu baru.", R.drawable.piano));
                    activityList.add(new ActivityItem("Membuat Lilin Aromaterapi", "Ciptakan suasana santai dengan lilin buatan sendiri.", R.drawable.comp));
                    activityList.add(new ActivityItem("Belajar Merakit Komputer", "Tingkatkan pengetahuan teknologi dengan mencoba merakit komputer.", R.drawable.eating));
                    activityList.add(new ActivityItem("Membuat Podcast", "Rekam cerita atau diskusi untuk podcast Anda sendiri.", R.drawable.ic_creative_video));
                    activityList.add(new ActivityItem("Mendesain Baju", "Kreasikan desain pakaian unik.", R.drawable.shir));
                    activityList.add(new ActivityItem("Mempelajari Seni Origami Modular", "Kreasi origami yang lebih kompleks dan menantang.", R.drawable.ori));
                    // Tambahkan lebih banyak aktivitas indoor jika perlu
                } else if ("outdoor".equalsIgnoreCase(prediction)) {
                    activityList.add(new ActivityItem("Jogging", "Aktivitas ringan untuk menjaga kebugaran.", R.drawable.running));
                    activityList.add(new ActivityItem("Bersepeda", "Nikmati udara segar sambil berolahraga.", R.drawable.bicycle));
                    activityList.add(new ActivityItem("Piknik", "Habiskan waktu bersama keluarga di taman.", R.drawable.picnic));
                    activityList.add(new ActivityItem("Berkemah", "Cobalah pengalaman menyenangkan di alam.", R.drawable.tent));
                    activityList.add(new ActivityItem("Mendaki Gunung", "Tantang diri Anda dengan mendaki gunung.", R.drawable.hiking));
                    activityList.add(new ActivityItem("Bermain Sepak Bola", "Aktivitas olahraga bersama teman.", R.drawable.ball));
                    activityList.add(new ActivityItem("Bermain Bulutangkis", "Olahraga menyenangkan yang cocok untuk semua usia.", R.drawable.badmin));
                    activityList.add(new ActivityItem("Pergi ke Gym", "Tingkatkan kebugaran Anda di gym.", R.drawable.ic_home_exercise));
                    activityList.add(new ActivityItem("Jalan-Jalan di Taman", "Santai dan nikmati pemandangan taman.", R.drawable.garden));
                    activityList.add(new ActivityItem("Fotografi", "Tangkap momen indah di luar ruangan.", R.drawable.ic_creative_video));
                    activityList.add(new ActivityItem("Berkebun", "Habiskan waktu merawat tanaman.", R.drawable.garden));
                    activityList.add(new ActivityItem("Bermain di Pantai", "Nikmati angin laut dan pasir pantai.", R.drawable.blok));
                    activityList.add(new ActivityItem("Menyusuri Kota", "Jelajahi tempat-tempat baru di kota Anda.", R.drawable.running));
                    activityList.add(new ActivityItem("Belanja di Pasar", "Cari barang-barang unik di pasar lokal.", R.drawable.cook));
                    activityList.add(new ActivityItem("Bermain Basket", "Olahraga seru dengan teman.", R.drawable.ball));
                    activityList.add(new ActivityItem("Bermain Frisbee", "Bersenang-senang dengan frisbee di taman.", R.drawable.kite));
                    activityList.add(new ActivityItem("Mencoba Olahraga Air", "Kayaking atau paddleboarding untuk pengalaman seru.", R.drawable.water));
                    activityList.add(new ActivityItem("Menunggang Kuda", "Aktivitas menyenangkan dan menantang.", R.drawable.horse));
                    activityList.add(new ActivityItem("Bermain Golf", "Santai sambil bermain golf di lapangan.", R.drawable.marbles));
                    activityList.add(new ActivityItem("Mengunjungi Kebun Binatang", "Habiskan waktu bersama keluarga melihat binatang.", R.drawable.eating));
                    activityList.add(new ActivityItem("Mengunjungi Museum", "Tingkatkan wawasan Anda dengan sejarah dan seni.", R.drawable.animal));
                    activityList.add(new ActivityItem("Berperahu", "Nikmati pemandangan air dengan perahu kecil.", R.drawable.boat));
                    activityList.add(new ActivityItem("Bermain di Taman Bermain", "Waktu menyenangkan untuk anak-anak dan keluarga.", R.drawable.picnic));
                    activityList.add(new ActivityItem("Lari Estafet", "Bermain lari bersama tim di luar ruangan.", R.drawable.running));
                    activityList.add(new ActivityItem("Lari Maraton", "Tantang diri Anda dengan berpartisipasi dalam maraton lokal.", R.drawable.running));
                    activityList.add(new ActivityItem("Berburu Harta Karun (Geocaching)", "Ikuti permainan berburu harta karun di alam terbuka.", R.drawable.trea));
                    activityList.add(new ActivityItem("Snorkeling", "Nikmati pemandangan bawah laut yang memukau.", R.drawable.snork));
                    activityList.add(new ActivityItem("Bermain Layang-Layang", "Nikmati angin di taman dengan bermain layang-layang.", R.drawable.kite));
                    activityList.add(new ActivityItem("Paralayang", "Coba sensasi terbang di udara dengan paralayang.", R.drawable.kite));
                    activityList.add(new ActivityItem("Mendayung di Sungai", "Habiskan waktu di air dengan mendayung.", R.drawable.boat));
                    activityList.add(new ActivityItem("Ikut Workshop Lukisan Alam", "Gabung kelas melukis pemandangan luar ruangan.", R.drawable.cand));
                    activityList.add(new ActivityItem("Mengikuti Pasar Minggu", "Nikmati suasana pasar pagi sambil mencari barang unik.", R.drawable.picnic));
                    activityList.add(new ActivityItem("Bermain Sepatu Roda", "Nikmati sensasi meluncur dengan sepatu roda.", R.drawable.roll));
                    activityList.add(new ActivityItem("Bersepeda Gunung", "Tantang diri Anda dengan trek sepeda gunung yang seru.", R.drawable.hiking));
                    activityList.add(new ActivityItem("Mengunjungi Taman Nasional", "Lihat keindahan alam di taman nasional terdekat.", R.drawable.eating));
                    activityList.add(new ActivityItem("Berpartisipasi dalam Lomba Lari", "Ikut serta dalam lomba lari komunitas.", R.drawable.running));
                    activityList.add(new ActivityItem("Latihan Panahan", "Asah keterampilan dengan mencoba panahan.", R.drawable.archer));
                    activityList.add(new ActivityItem("Memancing", "Habiskan waktu dengan memancing di danau atau sungai.", R.drawable.fish));
                    activityList.add(new ActivityItem("Mengikuti Kelas Yoga di Taman", "Relaksasi di luar ruangan dengan yoga di taman.", R.drawable.yoga));
                }
                // Tambahkan lebih banyak aktivitas outdoor jika perlu
            }

            Collections.shuffle(activityList);

            List<ActivityItem> randomActivities = activityList.subList(0, Math.min(5, activityList.size()));

            // Set up RecyclerView
            ActivityAdapter adapter = new ActivityAdapter(randomActivities);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

            btnBack.setOnClickListener(v -> onBackPressed());

        }
    }}
