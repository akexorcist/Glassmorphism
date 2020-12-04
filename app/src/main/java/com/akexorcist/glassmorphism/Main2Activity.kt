package com.akexorcist.glassmorphism

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.akexorcist.glassmorphism.databinding.ActivityMain2Binding
import com.akexorcist.glassmorphism.databinding.ViewInfoCard2Binding
import com.akexorcist.glassmorphism.databinding.ViewInfoCardBinding


class Main2Activity : AppCompatActivity() {
    private val binding: ActivityMain2Binding by lazy { ActivityMain2Binding.inflate(layoutInflater) }
    private val adapter: Awesome2PagerAdapter by lazy { Awesome2PagerAdapter(getPosts()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        binding.buttonEdit.setOnLongClickListener {
//            binding.layoutBackground.drawable
//        }
        binding.buttonEdit.setOnClickListener {


        }
        binding.root.doOnGlobalLayout {
            glassify()
        }

//        binding.scrollView.setOnScrollChangeListener { _, _, _, _, _ ->
//            glassify()
//        }

        binding.viewPager2.adapter = adapter
        binding.viewPager2.registerOnPageChangeCallback(onPageChangeCallback)
    }

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            binding.viewPager2.post {
                glassify()
            }
        }
    }

    private fun blur(bitmap: Bitmap, blurAmount: Int) {
        val renderScript = RenderScript.create(this)
        if (blurAmount == 0) return
        val input = Allocation.createFromBitmap(renderScript, bitmap)
        val output = Allocation.createTyped(renderScript, input.type)
        ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript)).apply {
            setRadius(blurAmount.toFloat())
            setInput(input)
            forEach(output)
        }
        output.copyTo(bitmap)
    }

    private fun glassify() {
        val backgroundBitmap = binding.layoutBackground.drawToBitmap().let {
            Bitmap.createScaledBitmap(it, it.width / 8, it.height / 8, false)
        }
        val foregroundBitmap = binding.layoutForeground.drawToBitmap().let {
            Bitmap.createScaledBitmap(it, it.width / 8, it.height / 8, false)
        }
        val timeStart = System.currentTimeMillis()
        val pixels = IntArray(backgroundBitmap.height * backgroundBitmap.width)
        foregroundBitmap.getPixels(pixels, 0, foregroundBitmap.width, 0, 0, foregroundBitmap.width, foregroundBitmap.height)
        foregroundBitmap.setPixels(pixels.map {
            if (it and 0x111111 == 0 && it shr 24 > 0) {
                Color.TRANSPARENT
            } else if (it != 0) {
                Color.WHITE
            } else {
                it
            }
        }.toIntArray(), 0, foregroundBitmap.width, 0, 0, foregroundBitmap.width, foregroundBitmap.height)

        blur(backgroundBitmap, 4)


        val blurPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        }
        val shapePaint = Paint().apply {
            isAntiAlias = true
            color = Color.WHITE
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        }
        Canvas(foregroundBitmap).apply {
//                drawRect(0f, 0f, width.toFloat(), height.toFloat(), shapePaint)
            drawBitmap(backgroundBitmap, 0f, 0f, blurPaint)
        }
        Log.e("Check", "Duration : ${System.currentTimeMillis() - timeStart}")
//            binding.imageViewPreview.setImageBitmap(foregroundBitmap)
        binding.imageViewBackground.setImageBitmap(foregroundBitmap)
//            binding.imageViewBackground.background = BitmapDrawable(resources, foregroundBitmap)
    }

    private fun getPosts(): List<Post2> {
        return listOf(
            Post2(
                title = "มาทำลองเล่น Sensor API บนแอนดรอยด์กัน",
                description = "บนแอนดรอยด์นั้นมี Sensor ต่างๆมากมายเพื่อช่วยเพิ่มลูกเล่นและความสามารถต่างๆให้กับ Android OS อีกทั้งยังทำเป็น Sensor API เพื่อให้นักพัฒนาสามารถสร้างแอปที่เรียกใช้งาน Sensor เหล่านั้นได้อีกด้วย"
            ),
            Post2(
                title = "Google Maps SDK for Android v3 - ตอนที่ 3 เริ่มต้นใช้งาน",
                description = "หลังจากที่ได้ API Key มาจาก Google Developer Console แล้ว ต่อไปก็ถึงเวลาของเรียกใช้งาน Maps SDK เพื่อแสดง Google Maps ในโปรเจคแอนดรอยด์แล้วล่ะ"
            ),
            Post2(
                title = "Google Maps SDK for Android v3 - ตอนที่ 2 การสร้าง API Key",
                description = "เพราะการจะใช้งาน Google Maps SDK ได้นั้น นักพัฒนาจะต้องสร้าง API Key จาก Google Developer Console เสียก่อน"
            ),
            Post2(
                title = "Google Maps SDK for Android v3 - ตอนที่ 1 ทำความรู้จักกับ Maps SDK",
                description = "การแสดงแผนที่บนแอปไม่ใช่เรื่องยากอีกต่อไป ขอแค่ใช้ Google Maps SDK (ตอนนี้เป็น v3 แล้วนะ)"
            ),
            Post2(
                title = "Interpolator สำหรับ Property Animation บน Android",
                description = "รู้หรือไม่ว่าการสร้าง Animation บนแอนดรอยด์ด้วยการใช้ Object Animator หรือ Value Animator นั้นสามารถกำหนดลักษณะการทำงานของ Animation ด้วย Interpolator ได้นะ"
            ),
            Post2(
                title = "เตรียมตัวให้พร้อมกับ Package Visibility ที่เพิ่มเข้ามาใน Android 11\n",
                description = "เพราะมันมีกำแพงบางๆคอยกั้นขวางระหว่างเราสอง และกำแพงนั้นมีชื่อว่า Privacy"
            ),
            Post2(
                title = "การทำให้ Foldable Android Emulator รองรับ Posture ในรูปแบบต่างๆ",
                description = "มาทำให้ Android Emulator เป็น Foldable Devices รองรับกับ Posture หลายๆแบบกันเถอะ"
            ),
            Post2(
                title = "วิธีการใช้งาน ViewBinding",
                description = "มาดูกันว่าการใช้งาน ViewBinding ในแต่ละแบบ มีวิธีเรียกใช้งานยังไงบ้าง"
            ),
            Post2(
                title = "รู้หรือยัง Android 11 มี Wireless Debugging ให้ใช้ด้วยนะ",
                description = "บน Android 11 ทีมพัฒนาได้เพิ่มฟีเจอร์ที่ชื่อ Wireless Debugging เพื่อช่วยให้นักพัฒนาเชื่อมต่อ ADB ผ่าน WiFi ได้ทันที ไม่จำเป็นต้องเสียบสาย USB ก่อนอีกต่อไป"
            )
        )
    }
}



class Awesome2PagerAdapter(private val posts: List<Post2>) : RecyclerView.Adapter<Awesome2ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Awesome2ViewHolder =
        Awesome2ViewHolder(ViewInfoCard2Binding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = posts.count()

    override fun onBindViewHolder(holder: Awesome2ViewHolder, position: Int) {
        posts.getOrNull(position)?.let { post ->
            holder.bind(post)
        }
    }
}

class Awesome2ViewHolder(private val binding: ViewInfoCard2Binding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post2) {
        binding.textViewTitle.text = post.title
        binding.textViewDescription.text = post.description
    }
}

data class Post2(
    val title: String?,
    val description: String?
)
