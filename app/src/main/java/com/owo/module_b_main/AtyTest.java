package com.owo.module_b_main;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.wao.dogcat.R;

import io.github.douglasjunior.androidSimpleTooltip.OverlayView;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltipUtils;

/**
 * @author XQF
 * @created 2017/5/5
 */
public class AtyTest extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_test_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        findViewById(R.id.btn_simple).setOnClickListener(this);
        findViewById(R.id.btn_animated).setOnClickListener(this);
        findViewById(R.id.btn_overlay).setOnClickListener(this);
        findViewById(R.id.btn_maxwidth).setOnClickListener(this);
        findViewById(R.id.btn_outside).setOnClickListener(this);
        findViewById(R.id.btn_inside).setOnClickListener(this);
        findViewById(R.id.btn_inside_modal).setOnClickListener(this);
        findViewById(R.id.btn_modal_custom).setOnClickListener(this);
        findViewById(R.id.btn_no_arrow).setOnClickListener(this);
        findViewById(R.id.btn_custom_arrow).setOnClickListener(this);
        findViewById(R.id.btn_dialog).setOnClickListener(this);
        findViewById(R.id.btn_center).setOnClickListener(this);
        findViewById(R.id.btn_overlay_rect).setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.fab) {
            new SimpleTooltip.Builder(this)
                    .anchorView(v)
                    .text("Floating Action Button")
                    .gravity(Gravity.START)
                    .onDismissListener(new SimpleTooltip.OnDismissListener() {
                        @Override
                        public void onDismiss(SimpleTooltip tooltip) {
                            System.out.println("dismiss " + tooltip);
                        }
                    })
                    .onShowListener(new SimpleTooltip.OnShowListener() {
                        @Override
                        public void onShow(SimpleTooltip tooltip) {
                            System.out.println("show " + tooltip);
                        }
                    })
                    .build()
                    .show();

        } else if (v.getId() == R.id.btn_simple) {
            new SimpleTooltip.Builder(this)
                    .anchorView(v)
                    .text(R.string.btn_simple)
                    .gravity(Gravity.END)
                    .build()
                    .show();

        } else if (v.getId() == R.id.btn_animated) {
            new SimpleTooltip.Builder(this)
                    .anchorView(v)
                    .text(R.string.btn_animated)
                    .gravity(Gravity.TOP)
                    .animated(true)
                    .build()
                    .show();

        } else if (v.getId() == R.id.btn_overlay) {
            new SimpleTooltip.Builder(this)
                    .anchorView(v)
                    .text(R.string.btn_overlay)
                    .gravity(Gravity.START)
                    .animated(true)
                    .transparentOverlay(false)
                    .build()
                    .show();

        } else if (v.getId() == R.id.btn_maxwidth) {
            new SimpleTooltip.Builder(this)
                    .anchorView(v)
                    .text(getString(R.string.btn_maxwidth) + getString(R.string.btn_maxwidth) + getString(R.string.btn_maxwidth) + getString(R.string.btn_maxwidth) + getString(R.string.btn_maxwidth))
                    .gravity(Gravity.END)
                    .maxWidth(R.dimen.simpletooltip_max_width)
                    .build()
                    .show();

        } else if (v.getId() == R.id.btn_outside) {
            new SimpleTooltip.Builder(this)
                    .anchorView(v)
                    .text(R.string.btn_outside)
                    .gravity(Gravity.BOTTOM)
                    .dismissOnOutsideTouch(true)
                    .dismissOnInsideTouch(false)
                    .build()
                    .show();

        } else if (v.getId() == R.id.btn_inside) {
            new SimpleTooltip.Builder(this)
                    .anchorView(v)
                    .text(R.string.btn_inside)
                    .gravity(Gravity.START)
                    .dismissOnOutsideTouch(false)
                    .dismissOnInsideTouch(true)
                    .build()
                    .show();

        } else if (v.getId() == R.id.btn_inside_modal) {
            new SimpleTooltip.Builder(this)
                    .anchorView(v)
                    .text(R.string.btn_inside_modal)
                    .gravity(Gravity.END)
                    .dismissOnOutsideTouch(false)
                    .dismissOnInsideTouch(true)
                    .modal(true)
                    .build()
                    .show();

        } else if (v.getId() == R.id.btn_modal_custom) {
            final SimpleTooltip tooltip = new SimpleTooltip.Builder(this)
                    .anchorView(v)
                    .text(R.string.btn_modal_custom)
                    .gravity(Gravity.TOP)
                    .dismissOnOutsideTouch(false)
                    .dismissOnInsideTouch(false)
                    .modal(true)
                    .animated(true)
                    .animationDuration(2000)
                    .animationPadding(SimpleTooltipUtils.pxFromDp(50))
                    .contentView(R.layout.tooltip_custom, R.id.tv_text)
                    .focusable(true)
                    .build();

            final EditText ed = tooltip.findViewById(R.id.ed_text);

            tooltip.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v2) {
                    if (tooltip.isShowing())
                        tooltip.dismiss();
                    new SimpleTooltip.Builder(v.getContext())
                            .anchorView(v)
                            .text(ed.getText())
                            .gravity(Gravity.BOTTOM)
                            .build()
                            .show();
                }
            });

            tooltip.show();
        } else if (v.getId() == R.id.btn_no_arrow) {
            new SimpleTooltip.Builder(this)
                    .anchorView(v)
                    .text(R.string.btn_no_arrow)
                    .gravity(Gravity.START)
                    .showArrow(false)
                    .modal(true)
                    .animated(true)
                    .build()
                    .show();

        } else if (v.getId() == R.id.btn_custom_arrow) {
            new SimpleTooltip.Builder(this)
                    .anchorView(v)
                    .text(R.string.btn_custom_arrow)
                    .gravity(Gravity.END)
                    .modal(true)
                    .arrowDrawable(android.R.drawable.ic_media_previous)
                    .arrowHeight((int) SimpleTooltipUtils.pxFromDp(50))
                    .arrowWidth((int) SimpleTooltipUtils.pxFromDp(50))
                    .build()
                    .show();

//        } else if (v.getId() == R.id.btn_dialog) {
//            final Dialog dialog = new Dialog(this);
//            dialog.setContentView(R.layout.dialog);
//            dialog.show();
//
//            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//            lp.copyFrom(dialog.getWindow().getAttributes());
//            lp.width = (int) SimpleTooltipUtils.pxFromDp(300);
//            lp.height = (int) SimpleTooltipUtils.pxFromDp(300);
//            dialog.getWindow().setAttributes(lp);
//
//            final Button btnInDialog = (Button) dialog.findViewById(R.id.btn_in_dialog);
//            btnInDialog.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    new SimpleTooltip.Builder(AtyTest.this)
//                            .anchorView(btnInDialog)
//                            .text(R.string.btn_in_dialog)
//                            .gravity(Gravity.BOTTOM)
//                            .animated(true)
//                            .transparentOverlay(false)
//                            .build()
//                            .show();
//                }
//            });
//            final Button btnClose = (Button) dialog.findViewById(R.id.btn_close);
//            btnClose.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//        }
        }
            else if (v.getId() == R.id.btn_center) {
                new SimpleTooltip.Builder(this)
                        .anchorView(v.getRootView())
                        .text(R.string.btn_center)
                        .showArrow(false)
                        .gravity(Gravity.CENTER)
                        .build()
                        .show();
            } else if (v.getId() == R.id.btn_overlay_rect) {
                new SimpleTooltip.Builder(this)
                        .anchorView(v)
                        .text(R.string.btn_overlay_rect)
                        .gravity(Gravity.END)
                        .animated(true)
                        .transparentOverlay(false)
                        .highlightShape(OverlayView.HIGHLIGHT_SHAPE_RECTANGULAR)
                        .overlayOffset(0)
                        .build()
                        .show();
            }
        }
    }