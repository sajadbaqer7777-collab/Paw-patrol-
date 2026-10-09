package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

object PupSoundEffects {
    private val scope = CoroutineScope(Dispatchers.Default)
    var isSoundEnabled: Boolean = true

    private fun playPcm(sampleRate: Int, samples: ShortArray) {
        if (!isSoundEnabled) return
        scope.launch {
            try {
                val minBufferSize = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
                val bufferSize = maxOf(minBufferSize, samples.size * 2)

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(samples, 0, samples.size)
                audioTrack.play()
                // Let it finish playing before release
                val durationMs = (samples.size.toDouble() / sampleRate * 1000).toLong() + 50
                Thread.sleep(durationMs)
                audioTrack.release()
            } catch (_: Exception) {
                // Ignore audio playback failure gracefully
            }
        }
    }

    /**
     * Bubbly popping sound with rapid upward frequency sweep.
     */
    fun playPop() {
        if (!isSoundEnabled) return
        val sampleRate = 22050
        val durationSec = 0.08
        val numSamples = (sampleRate * durationSec).toInt()
        val samples = ShortArray(numSamples)
        var phase = 0.0

        for (i in 0 until numSamples) {
            val progress = i.toDouble() / numSamples
            // Frequency sweeps from 400Hz to 1200Hz
            val freq = 400.0 + progress * 800.0
            phase += 2.0 * Math.PI * freq / sampleRate
            val envelope = (1.0 - progress) * (1.0 - progress)
            samples[i] = (sin(phase) * envelope * 24000).toInt().toShort()
        }
        playPcm(sampleRate, samples)
    }

    /**
     * Joyful success chime arpeggio: C5 - E5 - G5 - C6
     */
    fun playSuccessChime() {
        if (!isSoundEnabled) return
        val sampleRate = 22050
        val notes = doubleArrayOf(523.25, 659.25, 783.99, 1046.50) // C5, E5, G5, C6
        val noteDuration = 0.07
        val noteSamples = (sampleRate * noteDuration).toInt()
        val totalSamples = noteSamples * notes.size
        val samples = ShortArray(totalSamples)

        var idx = 0
        for (freq in notes) {
            var phase = 0.0
            for (i in 0 until noteSamples) {
                val progress = i.toDouble() / noteSamples
                phase += 2.0 * Math.PI * freq / sampleRate
                val envelope = sin(progress * Math.PI)
                samples[idx++] = (sin(phase) * envelope * 22000).toInt().toShort()
            }
        }
        playPcm(sampleRate, samples)
    }

    /**
     * Balloon burst sound: gentle noise transient + low thud.
     */
    fun playBalloonPop() {
        if (!isSoundEnabled) return
        val sampleRate = 22050
        val durationSec = 0.12
        val numSamples = (sampleRate * durationSec).toInt()
        val samples = ShortArray(numSamples)
        var phase = 0.0

        for (i in 0 until numSamples) {
            val progress = i.toDouble() / numSamples
            val freq = 900.0 - progress * 700.0
            phase += 2.0 * Math.PI * freq / sampleRate
            val noise = (Math.random() * 2.0 - 1.0) * 0.4
            val envelope = (1.0 - progress)
            val signal = (sin(phase) * 0.6 + noise) * envelope
            samples[i] = (signal * 24000).toInt().toShort()
        }
        playPcm(sampleRate, samples)
    }

    /**
     * Cute double puppy bark chirp!
     */
    fun playBark() {
        if (!isSoundEnabled) return
        val sampleRate = 22050
        val durationSec = 0.22
        val numSamples = (sampleRate * durationSec).toInt()
        val samples = ShortArray(numSamples)

        val woofLen = (sampleRate * 0.08).toInt()
        val gapLen = (sampleRate * 0.04).toInt()

        fun writeWoof(offset: Int, baseFreq: Double) {
            var phase = 0.0
            for (i in 0 until woofLen) {
                val progress = i.toDouble() / woofLen
                val freq = baseFreq + (1.0 - progress) * 200.0
                phase += 2.0 * Math.PI * freq / sampleRate
                val envelope = sin(progress * Math.PI)
                val sampleVal = (sin(phase) + 0.4 * sin(phase * 2.1)) * envelope * 18000
                if (offset + i < samples.size) {
                    samples[offset + i] = sampleVal.toInt().toShort()
                }
            }
        }

        writeWoof(0, 380.0)
        writeWoof(woofLen + gapLen, 440.0)

        playPcm(sampleRate, samples)
    }

    /**
     * Crunchy treat eating sound.
     */
    fun playCrunch() {
        if (!isSoundEnabled) return
        val sampleRate = 22050
        val durationSec = 0.1
        val numSamples = (sampleRate * durationSec).toInt()
        val samples = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val progress = i.toDouble() / numSamples
            val noise = (Math.random() * 2.0 - 1.0)
            val envelope = (1.0 - progress) * (1.0 - progress)
            samples[i] = (noise * envelope * 20000).toInt().toShort()
        }
        playPcm(sampleRate, samples)
    }

    /**
     * Grand mission complete fanfare!
     */
    fun playFanfare() {
        if (!isSoundEnabled) return
        val sampleRate = 22050
        val notes = doubleArrayOf(523.25, 659.25, 783.99, 1046.50, 1046.50, 1318.51)
        val durations = doubleArrayOf(0.08, 0.08, 0.08, 0.12, 0.08, 0.25)
        val totalSamples = (durations.sum() * sampleRate).toInt()
        val samples = ShortArray(totalSamples)

        var idx = 0
        for (n in notes.indices) {
            val freq = notes[n]
            val noteSamples = (sampleRate * durations[n]).toInt()
            var phase = 0.0
            for (i in 0 until noteSamples) {
                val progress = i.toDouble() / noteSamples
                phase += 2.0 * Math.PI * freq / sampleRate
                val envelope = sin(progress * Math.PI)
                if (idx < samples.size) {
                    samples[idx++] = (sin(phase) * envelope * 24000).toInt().toShort()
                }
            }
        }
        playPcm(sampleRate, samples)
    }

    /**
     * Friendly boing when attempting a non-match.
     */
    fun playBoing() {
        if (!isSoundEnabled) return
        val sampleRate = 22050
        val durationSec = 0.15
        val numSamples = (sampleRate * durationSec).toInt()
        val samples = ShortArray(numSamples)
        var phase = 0.0

        for (i in 0 until numSamples) {
            val progress = i.toDouble() / numSamples
            val freq = 220.0 + sin(progress * 15.0) * 80.0
            phase += 2.0 * Math.PI * freq / sampleRate
            val envelope = (1.0 - progress)
            samples[i] = (sin(phase) * envelope * 18000).toInt().toShort()
        }
        playPcm(sampleRate, samples)
    }
}
