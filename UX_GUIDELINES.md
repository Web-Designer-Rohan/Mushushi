# UX_GUIDELINES: MASHASHI VISUAL IDENTITY

## 1. DESIGN PHILOSOPHY: "THE VOID"
The design follows a "Dark Minimalism" approach. By using true blacks and high-contrast elements, we create a focused environment that reduces eye strain and emphasizes the user's progress.

## 2. COLOR PALETTE (Hex Codes)
* **Surface (Background):** `#000000` (True Black) — Essential for OLED battery saving and focus.
* **Elevated Surface:** `#0A0A0A` to `#121212` — Used for cards and bottom sheets to create depth.
* **Primary Accent:** `#FFFFFF` (Pure White) — Used for primary actions and headers.
* **Secondary Accent:** `#A1A1A1` (Slate Grey) — Used for secondary text and inactive states.
* **Growth/XP Color:** `#BFFF00` (Electric Lime) or `#00FFA3` (Neon Mint) — Used sparingly for "Level Up" and XP bars to signify energy.
* **Danger/Lockdown:** `#FF3B30` (System Red) — Used only for warnings or restricted access alerts.

## 3. TYPOGRAPHY
* **Primary Font:** `Inter` or `System Sans-Serif`.
* **Header Style:** Bold or Extra Bold, Tracking (Letter Spacing) set to `-0.02em` for a modern, compact look.
* **Body Style:** Regular or Medium, Tracking `0.01em` for readability.
* **Monospace:** Used for XP counters and Rank displays (e.g., `RANK: S+`) to give a "system/tech" vibe.

## 4. VISUAL ELEMENTS & EFFECTS

### A. Glassmorphism (The "Glass" Effect)
* Cards should have a subtle border: `1dp` width, color `White` with `10% Opacity`.
* Background Blur: Use a `20dp` blur radius for overlays and bottom navigation bars.

### B. Fluid Waveforms
* The waveforms from the reference image represent "Flow State."
* **Animation:** Use smooth, sine-wave oscillations.
* **Stroke:** Thin lines (`0.5dp` to `1dp`) with varying opacities to create a 3D sense of depth.

### C. Gamification UI
* **Rank Display:** Large, centered character (e.g., "S") with a glow effect (Outer Shadow).
* **Progress Bars:** Thin, sleek lines. No rounded corners on the bar itself—keep it sharp and "industrial."

## 5. INTERACTION DESIGN (Haptics & Motion)
* **Micro-interactions:** Every button press should trigger a "Light" haptic vibration.
* **Rank Up:** Trigger a "Heavy" haptic pulse and a full-screen particle burst.
* **Transitions:** Use "Staggered Fade-In" for list items. Elements should slide up slightly (`8dp`) as they appear.

## 6. THE "BLACKOUT" UX
* When the screen locks for NSFW or Night mode, the UI must be absolute:
    * No icons except the Dialer and WhatsApp.
    * A single, centered text: "EVOLVE OR REMAIN THE SAME."
    * No "Close" button. The only way out is the completion of the required task.

## 7. ICONOGRAPHY
* **Style:** Thin-stroke line icons (Linear icons).
* **Weight:** `1.5pt` stroke width.
* **Active State:** Icon fills with White or the Primary Accent color.
