package ru.tolsi.aik.geom.debug

data class Color private constructor(val r: UByte, val g: UByte, val b: UByte, val alpha: UByte = 255u) {
    companion object {
        operator fun invoke(r: Int, g: Int, b: Int): Color {
            return Color(r.toByte().toUByte(), g.toByte().toUByte(), b.toByte().toUByte())
        }

        val Maroon = Color.invoke(128, 0, 0)
        val DarkRed = Color.invoke(139, 0, 0)
        val Brown = Color.invoke(165, 42, 42)
        val Firebrick = Color.invoke(178, 34, 34)
        val Crimson = Color.invoke(220, 20, 60)
        val Red = Color.invoke(255, 0, 0)
        val Tomato = Color.invoke(255, 99, 71)
        val Coral = Color.invoke(255, 127, 80)
        val IndianRed = Color.invoke(205, 92, 92)
        val LightCoral = Color.invoke(240, 128, 128)
        val DarkSalmon = Color.invoke(233, 150, 122)
        val Salmon = Color.invoke(250, 128, 114)
        val LightSalmon = Color.invoke(255, 160, 122)
        val OrangeRed = Color.invoke(255, 69, 0)
        val DarkOrange = Color.invoke(255, 140, 0)
        val Orange = Color.invoke(255, 165, 0)
        val Gold = Color.invoke(255, 215, 0)
        val DarkGoldenRod = Color.invoke(184, 134, 11)
        val GoldenRod = Color.invoke(218, 165, 32)
        val PaleGoldenRod = Color.invoke(238, 232, 170)
        val DarkKhaki = Color.invoke(189, 183, 107)
        val Khaki = Color.invoke(240, 230, 140)
        val Olive = Color.invoke(128, 128, 0)
        val Yellow = Color.invoke(255, 255, 0)
        val YellowGreen = Color.invoke(154, 205, 50)
        val DarkOliveGreen = Color.invoke(85, 107, 47)
        val OliveDrab = Color.invoke(107, 142, 35)
        val LawnGreen = Color.invoke(124, 252, 0)
        val ChartReuse = Color.invoke(127, 255, 0)
        val GreenYellow = Color.invoke(173, 255, 47)
        val DarkGreen = Color.invoke(0, 100, 0)
        val Green = Color.invoke(0, 128, 0)
        val ForestGreen = Color.invoke(34, 139, 34)
        val Lime = Color.invoke(0, 255, 0)
        val LimeGreen = Color.invoke(50, 205, 50)
        val LightGreen = Color.invoke(144, 238, 144)
        val PaleGreed = Color.invoke(152, 251, 152)
        val DarkSeaGreed = Color.invoke(143, 188, 143)
        val MediumSpringGreed = Color.invoke(0, 250, 154)
        val SpringGreed = Color.invoke(0, 255, 127)
        val SeaGreed = Color.invoke(46, 139, 87)
        val MediumAquaMarine = Color.invoke(102, 205, 170)
        val MediumSeaGreed = Color.invoke(60, 179, 113)
        val LightSeaGreed = Color.invoke(32, 178, 170)
        val DarkSlateGray = Color.invoke(47, 79, 79)
        val Teal = Color.invoke(0, 128, 128)
        val DarkCyan = Color.invoke(0, 139, 139)
        val Aqua = Color.invoke(0, 255, 255)
        val Cyan = Color.invoke(0, 255, 255)
        val LightCyan = Color.invoke(224, 255, 255)
        val DarkTurquoise = Color.invoke(0, 206, 209)
        val Turquoise = Color.invoke(64, 224, 208)
        val MediumTurquoise = Color.invoke(72, 209, 204)
        val PaleTurquoise = Color.invoke(175, 238, 238)
        val AquaMarine = Color.invoke(127, 255, 212)
        val PowderBlue = Color.invoke(176, 224, 230)
        val CadetBlue = Color.invoke(95, 158, 160)
        val SteelBlue = Color.invoke(70, 130, 180)
        val CornFlowerBlue = Color.invoke(100, 149, 237)
        val DeepSkyBlue = Color.invoke(0, 191, 255)
        val DodgerBlue = Color.invoke(30, 144, 255)
        val LightBlue = Color.invoke(173, 216, 230)
        val SkyBlue = Color.invoke(135, 206, 235)
        val LightSkyBlue = Color.invoke(135, 206, 250)
        val MidnightBlue = Color.invoke(25, 25, 112)
        val Navy = Color.invoke(0, 0, 128)
        val DarkBlue = Color.invoke(0, 0, 139)
        val MediumBlue = Color.invoke(0, 0, 205)
        val Blue = Color.invoke(0, 0, 255)
        val RoyalBlue = Color.invoke(65, 105, 225)
        val BlueViolet = Color.invoke(138, 43, 226)
        val Indigo = Color.invoke(75, 0, 130)
        val DarkSlateBlue = Color.invoke(72, 61, 139)
        val SlateBlue = Color.invoke(106, 90, 205)
        val MediumSlateBlue = Color.invoke(123, 104, 238)
        val MediumPurple = Color.invoke(147, 112, 219)
        val DarkMagenta = Color.invoke(139, 0, 139)
        val DarkViolet = Color.invoke(148, 0, 211)
        val DarkOrchid = Color.invoke(153, 50, 204)
        val MediumOrchid = Color.invoke(186, 85, 211)
        val Purple = Color.invoke(128, 0, 128)
        val Thistle = Color.invoke(216, 191, 216)
        val Plum = Color.invoke(221, 160, 221)
        val Violet = Color.invoke(238, 130, 238)
        val Magenta = Color.invoke(255, 0, 255)
        val Orchid = Color.invoke(218, 112, 214)
        val MediumVioletRed = Color.invoke(199, 21, 133)
        val PaleVioletRed = Color.invoke(219, 112, 147)
        val DeepPink = Color.invoke(255, 20, 147)
        val HotPink = Color.invoke(255, 105, 180)
        val LightPink = Color.invoke(255, 182, 193)
        val Pink = Color.invoke(255, 192, 203)
        val AntiqueWhite = Color.invoke(250, 235, 215)
        val Beige = Color.invoke(245, 245, 220)
        val Bisque = Color.invoke(255, 228, 196)
        val BlanchedAlmond = Color.invoke(255, 235, 205)
        val Wheat = Color.invoke(245, 222, 179)
        val CornSilk = Color.invoke(255, 248, 220)
        val LemonChiffon = Color.invoke(255, 250, 205)
        val LightGoldenRodYellow = Color.invoke(250, 250, 210)
        val LightYellow = Color.invoke(255, 255, 224)
        val SaddleBrown = Color.invoke(139, 69, 19)
        val Sienna = Color.invoke(160, 82, 45)
        val Chocolate = Color.invoke(210, 105, 30)
        val Peru = Color.invoke(205, 133, 63)
        val SandyBrown = Color.invoke(244, 164, 96)
        val BurlyWood = Color.invoke(222, 184, 135)
        val Tan = Color.invoke(210, 180, 140)
        val RosyBrown = Color.invoke(188, 143, 143)
        val Moccasin = Color.invoke(255, 228, 181)
        val NavajoWhite = Color.invoke(255, 222, 173)
        val PeachPuff = Color.invoke(255, 218, 185)
        val MistyRose = Color.invoke(255, 228, 225)
        val LavenderBlush = Color.invoke(255, 240, 245)
        val Linen = Color.invoke(250, 240, 230)
        val OldLace = Color.invoke(253, 245, 230)
        val PapayaWhip = Color.invoke(255, 239, 213)
        val SeaShell = Color.invoke(255, 245, 238)
        val MintCream = Color.invoke(245, 255, 250)
        val SlateGray = Color.invoke(112, 128, 144)
        val LightSlateGray = Color.invoke(119, 136, 153)
        val LightSteelBlue = Color.invoke(176, 196, 222)
        val Lavender = Color.invoke(230, 230, 250)
        val FloralWhite = Color.invoke(255, 250, 240)
        val AliceBlue = Color.invoke(240, 248, 255)
        val GhostWhite = Color.invoke(248, 248, 255)
        val Honeydew = Color.invoke(240, 255, 240)
        val Ivory = Color.invoke(255, 255, 240)
        val Azure = Color.invoke(240, 255, 255)
        val Snow = Color.invoke(255, 250, 250)
        val Black = Color.invoke(0, 0, 0)
        val DimGray = Color.invoke(105, 105, 105)
        val Gray = Color.invoke(128, 128, 128)
        val DarkGray = Color.invoke(169, 169, 169)
        val Silver = Color.invoke(192, 192, 192)
        val LightGray = Color.invoke(211, 211, 211)
        val Gainsboro = Color.invoke(220, 220, 220)
        val WhiteSmoke = Color.invoke(245, 245, 245)
        val White = Color.invoke(255, 255, 255)

        val values = listOf(
            Maroon,
            DarkRed,
            Brown,
            Firebrick,
            Crimson,
            Red,
            Tomato,
            Coral,
            IndianRed,
            LightCoral,
            DarkSalmon,
            Salmon,
            LightSalmon,
            OrangeRed,
            DarkOrange,
            Orange,
            Gold,
            DarkGoldenRod,
            GoldenRod,
            PaleGoldenRod,
            DarkKhaki,
            Khaki,
            Olive,
            Yellow,
            YellowGreen,
            DarkOliveGreen,
            OliveDrab,
            LawnGreen,
            ChartReuse,
            GreenYellow,
            DarkGreen,
            Green,
            ForestGreen,
            Lime,
            LimeGreen,
            LightGreen,
            PaleGreed,
            DarkSeaGreed,
            MediumSpringGreed,
            SpringGreed,
            SeaGreed,
            MediumAquaMarine,
            MediumSeaGreed,
            LightSeaGreed,
            DarkSlateGray,
            Teal,
            DarkCyan,
            Aqua,
            Cyan,
            LightCyan,
            DarkTurquoise,
            Turquoise,
            MediumTurquoise,
            PaleTurquoise,
            AquaMarine,
            PowderBlue,
            CadetBlue,
            SteelBlue,
            CornFlowerBlue,
            DeepSkyBlue,
            DodgerBlue,
            LightBlue,
            SkyBlue,
            LightSkyBlue,
            MidnightBlue,
            Navy,
            DarkBlue,
            MediumBlue,
            Blue,
            RoyalBlue,
            BlueViolet,
            Indigo,
            DarkSlateBlue,
            SlateBlue,
            MediumSlateBlue,
            MediumPurple,
            DarkMagenta,
            DarkViolet,
            DarkOrchid,
            MediumOrchid,
            Purple,
            Thistle,
            Plum,
            Violet,
            Magenta,
            Orchid,
            MediumVioletRed,
            PaleVioletRed,
            DeepPink,
            HotPink,
            LightPink,
            Pink,
            AntiqueWhite,
            Beige,
            Bisque,
            BlanchedAlmond,
            Wheat,
            CornSilk,
            LemonChiffon,
            LightGoldenRodYellow,
            LightYellow,
            SaddleBrown,
            Sienna,
            Chocolate,
            Peru,
            SandyBrown,
            BurlyWood,
            Tan,
            RosyBrown,
            Moccasin,
            NavajoWhite,
            PeachPuff,
            MistyRose,
            LavenderBlush,
            Linen,
            OldLace,
            PapayaWhip,
            SeaShell,
            MintCream,
            SlateGray,
            LightSlateGray,
            LightSteelBlue,
            Lavender,
            FloralWhite,
            AliceBlue,
            GhostWhite,
            Honeydew,
            Ivory,
            Azure,
            Snow,
            Black,
            DimGray,
            Gray,
            DarkGray,
            Silver,
            LightGray,
            Gainsboro,
            WhiteSmoke,
            White
        )
    }
}