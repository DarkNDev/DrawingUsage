package com.darkndev.drawingusage

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.darkndev.drawingusage.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomSheetBehaviorScribble =
            BottomSheetBehavior.from(binding.strokeBottomSheet)

        bottomSheetBehaviorScribble.state = BottomSheetBehavior.STATE_HIDDEN

        binding.apply {
            openBottomSheet.setOnClickListener {
                bottomSheetBehaviorScribble.state = BottomSheetBehavior.STATE_EXPANDED
            }

            dragHandle.setOnClickListener {
                bottomSheetBehaviorScribble.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        val callback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                bottomSheetBehaviorScribble.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        bottomSheetBehaviorScribble.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    callback.isEnabled = true
                    binding.openBottomSheet.visibility = View.GONE
                }
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    callback.isEnabled = false
                    binding.openBottomSheet.visibility = View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        onBackPressedDispatcher.addCallback(callback)

        strokeSettings()
    }

    private fun strokeSettings() {
        binding.apply {
            colorButtonGroup.setOnSelectListener {
                drawingView.strokeColor = it.selectedBgColor
            }
            colorButtonGroup.selectButton(binding.blackColor)

            strokeWidthSlider.addOnChangeListener { _, value, _ ->
                drawingView.strokeWidth = value
            }

            undo.setOnClickListener {
                drawingView.undoStroke()
            }

            redo.setOnClickListener {
                drawingView.redoStroke()
            }

            clear.setOnClickListener {
                drawingView.clearCanvas()
            }
        }
    }
}