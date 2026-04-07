package com.rs.sample.android.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.rs.sample.android.R
import kotlinx.coroutines.launch
import sentinel.Sentinel
import sentinel.all
import sentinel.configure
import sentinel.core.ext.toByteList
import sentinel.core.logger.SentinelLogger
import sentinel.core.logger.SentinelLogger.info

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sentinel = Sentinel.configure(context = requireContext()) {
            config {
                this.appId = Sentinel.Identity.appId.toByteList()
                this.signature = Sentinel.Identity.signature?.toByteList()
                this.threshold = 20
                // this.isLoggingEnabled = true
            }

            all()
            // root()
            // tamper()
            // hook()
            // emulator()
            // debug()
            // location()
        }

        lifecycleScope.launch {
            val report = sentinel.inspect()
            SentinelLogger.report(report = report)
        }

        sentinel.runtime {
            onCompromised {
                info(msg = "Device integrity failed (Root/Jailbreak detected).")
            }

            onTampered {
                info(msg = "App tampering detected.")
            }

            onHooked {
                info(msg = "Runtime hook detection.")
            }

            onSimulated {
                info(msg = "Running on Emulator/Simulator environment.")
            }

            onDebugged {
                info(msg = "Active debugging session detected.")
            }

            onCritical { score ->
                info(msg = "High risk score reached: $score")
            }

            onSafe {
                info(msg = "All systems nominal.")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    companion object {

        fun newInstance() = MainFragment()
    }
}