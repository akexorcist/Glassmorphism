package com.akexorcist.glassmorphism

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.akexorcist.glassmorphism.databinding.ActivityMainBinding
import com.akexorcist.glassmorphism.databinding.ViewInfoCardBinding
import kotlin.math.abs


class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
//    private val adapter: AwesomePagerAdapter by lazy { AwesomePagerAdapter(getPosts()) }
    private var dX = 0f
    private var dY = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.glassViewTop.setOnTouchListener(onViewTouchListener)
        binding.glassViewBottom.setOnTouchListener(onViewTouchListener)
//        binding.viewPager2.adapter = adapter
//        binding.viewPager2.registerOnPageChangeCallback(onPageChangeCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
//        binding.viewPager2.unregisterOnPageChangeCallback(onPageChangeCallback)
    }

//    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
//        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//            binding.viewPager2.post {
//                adapter.notifyItemChanged(position)
//            }
//        }
//    }

    private val onViewTouchListener = { view: View, event: MotionEvent ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dX = view.x - event.rawX
                dY = view.y - event.rawY
                true
            }
            MotionEvent.ACTION_MOVE -> {
                view.animate()
                    .x(event.rawX + dX)
                    .y(event.rawY + dY)
                    .setDuration(0)
                    .start()
                binding.glassLayout.invalidate()
                binding.glassLayout.onBitmapUpdated = {
                    binding.imageViewPreview.setImageBitmap(it)
                }
                true
            }
            else -> false
        }
    }

    private fun getPosts(): List<Post> {
        return listOf(
            Post(
                title = "มาทำลองเล่น Sensor API บนแอนดรอยด์กัน",
                description = "บนแอนดรอยด์นั้นมี Sensor ต่างๆมากมายเพื่อช่วยเพิ่มลูกเล่นและความสามารถต่างๆให้กับ Android OS อีกทั้งยังทำเป็น Sensor API เพื่อให้นักพัฒนาสามารถสร้างแอปที่เรียกใช้งาน Sensor เหล่านั้นได้อีกด้วย"
            ),
            Post(
                title = "Google Maps SDK for Android v3 - ตอนที่ 3 เริ่มต้นใช้งาน",
                description = "หลังจากที่ได้ API Key มาจาก Google Developer Console แล้ว ต่อไปก็ถึงเวลาของเรียกใช้งาน Maps SDK เพื่อแสดง Google Maps ในโปรเจคแอนดรอยด์แล้วล่ะ"
            ),
            Post(
                title = "Google Maps SDK for Android v3 - ตอนที่ 2 การสร้าง API Key",
                description = "เพราะการจะใช้งาน Google Maps SDK ได้นั้น นักพัฒนาจะต้องสร้าง API Key จาก Google Developer Console เสียก่อน"
            ),
            Post(
                title = "Google Maps SDK for Android v3 - ตอนที่ 1 ทำความรู้จักกับ Maps SDK",
                description = "การแสดงแผนที่บนแอปไม่ใช่เรื่องยากอีกต่อไป ขอแค่ใช้ Google Maps SDK (ตอนนี้เป็น v3 แล้วนะ)"
            ),
            Post(
                title = "Interpolator สำหรับ Property Animation บน Android",
                description = "รู้หรือไม่ว่าการสร้าง Animation บนแอนดรอยด์ด้วยการใช้ Object Animator หรือ Value Animator นั้นสามารถกำหนดลักษณะการทำงานของ Animation ด้วย Interpolator ได้นะ"
            ),
            Post(
                title = "เตรียมตัวให้พร้อมกับ Package Visibility ที่เพิ่มเข้ามาใน Android 11\n",
                description = "เพราะมันมีกำแพงบางๆคอยกั้นขวางระหว่างเราสอง และกำแพงนั้นมีชื่อว่า Privacy"
            ),
            Post(
                title = "การทำให้ Foldable Android Emulator รองรับ Posture ในรูปแบบต่างๆ",
                description = "มาทำให้ Android Emulator เป็น Foldable Devices รองรับกับ Posture หลายๆแบบกันเถอะ"
            ),
            Post(
                title = "วิธีการใช้งาน ViewBinding",
                description = "มาดูกันว่าการใช้งาน ViewBinding ในแต่ละแบบ มีวิธีเรียกใช้งานยังไงบ้าง"
            ),
            Post(
                title = "รู้หรือยัง Android 11 มี Wireless Debugging ให้ใช้ด้วยนะ",
                description = "บน Android 11 ทีมพัฒนาได้เพิ่มฟีเจอร์ที่ชื่อ Wireless Debugging เพื่อช่วยให้นักพัฒนาเชื่อมต่อ ADB ผ่าน WiFi ได้ทันที ไม่จำเป็นต้องเสียบสาย USB ก่อนอีกต่อไป"
            )
        )
    }
}

class AwesomePagerAdapter(private val posts: List<Post>) : RecyclerView.Adapter<AwesomeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AwesomeViewHolder =
        AwesomeViewHolder(ViewInfoCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = posts.count()

    override fun onBindViewHolder(holder: AwesomeViewHolder, position: Int) {
        posts.getOrNull(position)?.let { post ->
            holder.bind(post)
        }
    }
}

class AwesomeViewHolder(private val binding: ViewInfoCardBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.textViewTitle.text = post.title
        binding.textViewDescription.text = post.description
    }
}

data class Post(
    val title: String?,
    val description: String?
)