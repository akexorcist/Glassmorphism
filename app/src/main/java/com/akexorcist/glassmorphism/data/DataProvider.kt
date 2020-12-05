package com.akexorcist.glassmorphism.data

import com.akexorcist.glassmorphism.R

class DataProvider {
    fun getHeader(): Profile.Header {
        return Profile.Header(
            name = "Sleeping For Less",
            description = "Android development website for Android developers"
        )
    }

    fun getSocials(): List<Profile.Social> {
        return listOf(
            Profile.Social(
                name = "Facebook",
                icon = R.drawable.ic_facebook,
                url = ""
            ),
            Profile.Social(
                name = "GitHub",
                icon = R.drawable.ic_github,
                url = ""
            ),
            Profile.Social(
                name = "Instagram",
                icon = R.drawable.ic_instagram,
                url = ""
            ),
            Profile.Social(
                name = "LinkedIn",
                icon = R.drawable.ic_linkedin,
                url = ""
            ),
            Profile.Social(
                name = "Twitter",
                icon = R.drawable.ic_twitter,
                url = ""
            ),
            Profile.Social(
                name = "YouTube",
                icon = R.drawable.ic_youtube,
                url = ""
            )
        )
    }

    fun getPosts(): List<Profile.Post> {
        return listOf(
            Profile.Post(
                title = "มาทำลองเล่น Sensor API บนแอนดรอยด์กัน",
                description = "บนแอนดรอยด์นั้นมี Sensor ต่างๆมากมายเพื่อช่วยเพิ่มลูกเล่นและความสามารถต่างๆให้กับ Android OS อีกทั้งยังทำเป็น Sensor API เพื่อให้นักพัฒนาสามารถสร้างแอปที่เรียกใช้งาน Sensor เหล่านั้นได้อีกด้วย"
            ),
            Profile.Post(
                title = "Google Maps SDK for Android v3 - ตอนที่ 3 เริ่มต้นใช้งาน",
                description = "หลังจากที่ได้ API Key มาจาก Google Developer Console แล้ว ต่อไปก็ถึงเวลาของเรียกใช้งาน Maps SDK เพื่อแสดง Google Maps ในโปรเจคแอนดรอยด์แล้วล่ะ"
            ),
            Profile.Post(
                title = "Google Maps SDK for Android v3 - ตอนที่ 2 การสร้าง API Key",
                description = "เพราะการจะใช้งาน Google Maps SDK ได้นั้น นักพัฒนาจะต้องสร้าง API Key จาก Google Developer Console เสียก่อน"
            ),
            Profile.Post(
                title = "Google Maps SDK for Android v3 - ตอนที่ 1 ทำความรู้จักกับ Maps SDK",
                description = "การแสดงแผนที่บนแอปไม่ใช่เรื่องยากอีกต่อไป ขอแค่ใช้ Google Maps SDK (ตอนนี้เป็น v3 แล้วนะ)"
            ),
            Profile.Post(
                title = "Interpolator สำหรับ Property Animation บน Android",
                description = "รู้หรือไม่ว่าการสร้าง Animation บนแอนดรอยด์ด้วยการใช้ Object Animator หรือ Value Animator นั้นสามารถกำหนดลักษณะการทำงานของ Animation ด้วย Interpolator ได้นะ"
            ),
            Profile.Post(
                title = "เตรียมตัวให้พร้อมกับ Package Visibility ที่เพิ่มเข้ามาใน Android 11\n",
                description = "เพราะมันมีกำแพงบางๆคอยกั้นขวางระหว่างเราสอง และกำแพงนั้นมีชื่อว่า Privacy"
            ),
            Profile.Post(
                title = "การทำให้ Foldable Android Emulator รองรับ Posture ในรูปแบบต่างๆ",
                description = "มาทำให้ Android Emulator เป็น Foldable Devices รองรับกับ Posture หลายๆแบบกันเถอะ"
            ),
            Profile.Post(
                title = "วิธีการใช้งาน ViewBinding",
                description = "มาดูกันว่าการใช้งาน ViewBinding ในแต่ละแบบ มีวิธีเรียกใช้งานยังไงบ้าง"
            ),
            Profile.Post(
                title = "รู้หรือยัง Android 11 มี Wireless Debugging ให้ใช้ด้วยนะ",
                description = "บน Android 11 ทีมพัฒนาได้เพิ่มฟีเจอร์ที่ชื่อ Wireless Debugging เพื่อช่วยให้นักพัฒนาเชื่อมต่อ ADB ผ่าน WiFi ได้ทันที ไม่จำเป็นต้องเสียบสาย USB ก่อนอีกต่อไป"
            ),
            Profile.Post(
                title = "ภาพไอคอนของแอปบนแอนดรอยด์ที่นักพัฒนาควรรู้",
                description = "App Icon หรือภาพไอคอนของแอปก็เป็นอีกอย่างหนึ่งที่ถือว่าเป็นสิ่งสำคัญสำหรับแอปแทบทุกตัว เพราะภาพไอคอนสามารถสร้างภาพจำให้แก่ผู้ใช้ได้ โดยในบทความนี้จะพามาดูกันว่าการกำหนดภาพไอคอนในแอปบนแอนดรอยด์นั้นมีอะไรบ้างที่นักพัฒนาควรรู้"
            ),
            Profile.Post(
                title = "Target API Level บนแอนดรอยด์นั้นสำคัญแค่ไหน?",
                description = "ส่วนใหญ่ก็จะคุ้นเคยกับคำว่า Minimum SDK Version กันอยู่แล้วเนอะ แต่ในบทความนี้จะมาพูดถึงอีกตัวหนึ่งที่สำคัญไม่แพ้กัน โดยมีชื่อเรียกว่า Target API Level"
            ),
            Profile.Post(
                title = "ว่าด้วยเรื่อง Copy ใน Data Class ของ Kotlin",
                description = "มาดูกันว่า Copy ใน Data Class ของภาษา Kotlin นั้นทำอะไรได้บ้าง และมีข้อจำกัดอะไรที่นักพัฒนาควรรู้ไว้"
            ),
            Profile.Post(
                title = "จัดการกับ State Changes ใน Custom View อย่างไรให้ครอบคลุม (รวมไปถึง Inherited Custom View)",
                description = "State Changes ถือว่าเป็นเรื่องพื้นฐานที่นักพัฒนาแอนดรอยด์ควรจัดการทุกครั้ง มาดูวิธีการจัดการกับ State Changes ในระดับของ View บน Custom View อย่างถูกต้องกัน"
            ),
            Profile.Post(
                title = "ทำเว็ปให้รองรับในแอปแอนดรอยด์แบบเท่ๆด้วย Trusted Web Activity",
                description = "ในยุคที่เว็ปสามารถทำอะไรได้มากขึ้นกว่าเมื่อก่อน จึงทำให้นักพัฒนาบางคนตัดสินใจที่จะพัฒนาเว็ปเพื่อใช้งานในแอปแทนที่จะเขียนโค้ด Native"
            ),
            Profile.Post(
                title = "Google Play Console สมัยนี้มีอะไรเจ๋งๆสำหรับนักพัฒนาบ้าง",
                description = "มาดูกันว่าในยุคนี้ Google Play Console มีอะไรให้ใช้กันบ้าง"
            ),
            Profile.Post(
                title = "Android App Bundle — ตอนที่ 2 สิ่งที่ควรรู้ในการใช้งาน Android App Bundle",
                description = "มาดูกันว่ามีอะไรบ้างที่นักพัฒนาต้องรู้บ้าง เพื่อให้ให้แอปสามารถรองรับ Android App Bundle ได้อย่างสบายใจ"
            ),
            Profile.Post(
                title = "Android App Bundle — ตอนที่ 1 รู้จักความสามารถและเบื้องหลังในการทำงาน",
                description = "วันนี้ขอหยิบเรื่องราวต่างๆของ Android App Bundle เพื่อให้เข้าใจกันมากขึ้นครับ"
            ),
            Profile.Post(
                title = "CameraX ตอนที่ 2 — ใช้งาน CameraX แบบง่ายๆด้วย CameraView",
                description = "หลังจากที่ได้รู้เรื่องราวของ CameraX กันแล้ว มาดูกันว่าการใช้งาน CameraX ด้วยวิธีที่ง่ายที่สุดนั้น จะง่ายซักแค่ไหนกันเชียว"
            ),
            Profile.Post(
                title = "CameraX ตอนที่ 1 — รู้จัก CameraX กันแล้วหรือยัง?",
                description = "ในที่สุดก็ถึงเวลาอันเหมาะสมที่จะหยิบเรื่องราวของ CameraX มาเล่าสู่กันฟังเสียที เพราะนี่คือ 1 ในไลบรารีที่เจ้าของบล็อกรอคอยมากๆตัวหนึ่งเลยก็ว่าได้"
            )
        )
    }
}